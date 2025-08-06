package ru.practicum.requestservice.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.interaction.dto.event.EventFullDto;
import ru.practicum.interaction.dto.event.enums.EventState;
import ru.practicum.interaction.dto.request.RequestDto;
import ru.practicum.interaction.dto.request.RequestUpdateDto;
import ru.practicum.interaction.dto.request.RequestUpdateResultDto;
import ru.practicum.interaction.dto.user.UserShortDto;
import ru.practicum.interaction.exception.ConflictException;
import ru.practicum.interaction.exception.ForbiddenOperationException;
import ru.practicum.interaction.exception.NotFoundException;
import ru.practicum.interaction.feign.EventFeignClient;
import ru.practicum.interaction.feign.UserFeignClient;
import ru.practicum.requestservice.mapper.RequestMapper;
import ru.practicum.requestservice.model.Request;
import ru.practicum.requestservice.model.QRequest;
import ru.practicum.interaction.dto.request.enums.RequestStatus;
import ru.practicum.requestservice.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserFeignClient userFeignClient;
    private final EventFeignClient eventFeignClient;
    private final RequestMapper requestMapper;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RequestDto> getUserRequests(Long userId) {
        checkUserExists(userId);
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(requestMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public RequestDto createRequest(Long userId, Long eventId) {
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Запрос уже существует");
        }

        UserShortDto requester = getUserShortOrThrow(userId);
        EventFullDto event = getEventOrThrow(eventId);

        if (requester.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException("Инициатор события не может подать запрос на участие");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Событие ещё не опубликовано");
        }

        long confirmedRequests = requestRepository.countAllByEventIdAndStatusIs(eventId, RequestStatus.CONFIRMED);
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() == confirmedRequests) {
            throw new ConflictException("Лимит участников исчерпан");
        }

        Request request = requestMapper.toEntity(event, requester);
        request.setCreated(LocalDateTime.now());
        request.setStatus(
                !event.isRequestModeration() || event.getParticipantLimit() == 0
                        ? RequestStatus.CONFIRMED
                        : RequestStatus.PENDING
        );

        return requestMapper.toDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public RequestDto cancelRequest(Long userId, Long requestId) {
        checkUserExists(userId);

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос с id = %s не найден", requestId)));

        if (!userId.equals(request.getRequesterId())) {
            throw new ForbiddenOperationException("Отменять запрос может только его автор");
        }

        if (request.getStatus().equals(RequestStatus.PENDING)) {
            request.setStatus(RequestStatus.CANCELED);
        }

        return requestMapper.toDto(request);
    }

    @Override
    public List<RequestDto> getEventRequests(Long userId, Long eventId) {
        checkUserExists(userId);
        checkUserIsEventInitiator(userId, eventId);

        return requestRepository.findAllByEventId(eventId).stream()
                .map(requestMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public RequestUpdateResultDto updateEventRequests(Long userId, Long eventId, RequestUpdateDto updateDto) {
        checkUserExists(userId);
        EventFullDto event = getEventOrThrow(eventId);
        validateInitiator(userId, event);

        List<Request> requests = validateAndGetRequests(updateDto.getRequestIds(), eventId);
        validateRequestStatus(requests, updateDto.getStatus());

        long remainingLimit = calculateRemainingLimit(event, eventId);
        updateRequestStatuses(requests, updateDto.getStatus(), remainingLimit);

        return buildUpdateResult(requests);
    }

    private void validateInitiator(Long userId, EventFullDto event) {
        if (!userId.equals(event.getInitiator().getId())) {
            throw new ForbiddenOperationException("Редактировать запросы может только инициатор события");
        }
    }

    private List<Request> validateAndGetRequests(List<Long> requestIds, Long eventId) {
        List<Request> requests = requestRepository.findAllByIdInAndEventIdIs(requestIds, eventId);
        if (requests.size() != requestIds.size()) {
            throw new IllegalArgumentException("Найдены не все запросы");
        }
        return requests;
    }

    private void validateRequestStatus(List<Request> requests, RequestStatus newStatus) {
        if (newStatus == RequestStatus.REJECTED && requests.stream().anyMatch(r -> r.getStatus() == RequestStatus.CONFIRMED)) {
            throw new ConflictException("Нельзя отменить уже подтвержденную заявку");
        }
    }

    private long calculateRemainingLimit(EventFullDto event, Long eventId) {
        return event.getParticipantLimit() - requestRepository.countAllByEventIdAndStatusIs(eventId, RequestStatus.CONFIRMED);
    }

    private void updateRequestStatuses(List<Request> requests, RequestStatus newStatus, long remainingLimit) {
        if (newStatus == RequestStatus.CONFIRMED) {
            updateConfirmedRequests(requests, remainingLimit);
        } else if (newStatus == RequestStatus.REJECTED) {
            updateRejectedRequests(requests);
        }
    }

    private void updateConfirmedRequests(List<Request> requests, long remainingLimit) {
        if (remainingLimit <= 0) {
            throw new ConflictException("Лимит участников исчерпан");
        }
        long currentLimit = remainingLimit;
        for (Request request : requests) {
            if (request.getStatus() == RequestStatus.PENDING) {
                request.setStatus(currentLimit > 0 ? RequestStatus.CONFIRMED : RequestStatus.REJECTED);
                currentLimit--;
            }
        }
    }

    private void updateRejectedRequests(List<Request> requests) {
        for (Request request : requests) {
            if (request.getStatus() == RequestStatus.PENDING) {
                request.setStatus(RequestStatus.REJECTED);
            }
        }
    }

    private RequestUpdateResultDto buildUpdateResult(List<Request> requests) {
        Map<Boolean, List<Request>> partitioned = requests.stream()
                .collect(Collectors.partitioningBy(r -> r.getStatus() == RequestStatus.CONFIRMED));
        return new RequestUpdateResultDto(
                partitioned.get(true).stream().map(requestMapper::toDto).toList(),
                partitioned.get(false).stream().map(requestMapper::toDto).toList()
        );
    }

    @Override
    public Map<Long, Long> getConfirmedRequests(List<Long> eventIds) {
        QRequest qRequest = QRequest.request;
        return jpaQueryFactory
                .select(qRequest.eventId, qRequest.count())
                .from(qRequest)
                .where(qRequest.eventId.in(eventIds)
                        .and(qRequest.status.eq(RequestStatus.CONFIRMED)))
                .groupBy(qRequest.eventId)
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(qRequest.eventId),
                        tuple -> Optional.ofNullable(tuple.get(1, Long.class)).orElse(0L)
                ));
    }

    @Override
    public Long countAllByEventIdAndStatus(Long eventId, String requestStatus) {
        return requestRepository.countAllByEventIdAndStatusIs(
                eventId,
                RequestStatus.valueOf(requestStatus)
        );
    }

    // Вспомогательные методы
    private UserShortDto getUserShortOrThrow(Long userId) {
        try {
            return userFeignClient.getUserShortById(userId);
        } catch (FeignException.NotFound e) {
            throw new NotFoundException(String.format("Пользователь с id = %s не найден", userId));
        }
    }

    private EventFullDto getEventOrThrow(Long eventId) {
        try {
            return eventFeignClient.getEventById(eventId);
        } catch (FeignException.NotFound e) {
            throw new NotFoundException(String.format("Событие с id = %s не найдено", eventId));
        }
    }

    private void checkUserExists(Long userId) {
        try {
            userFeignClient.getUserShortById(userId);
        } catch (FeignException.NotFound e) {
            throw new NotFoundException(String.format("Пользователь с id = %s не найден", userId));
        }
    }

    private void checkUserIsEventInitiator(Long userId, Long eventId) {
        EventFullDto event = getEventOrThrow(eventId);
        if (!userId.equals(event.getInitiator().getId())) {
            throw new ForbiddenOperationException("Доступ только для инициатора события");
        }
    }

    @Override
    public boolean isRequestExist(Long userId, Long eventId) {
        return requestRepository.existsByRequesterIdAndEventId(userId, eventId);
    }
}