package ru.practicum.eventservice.events.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import ru.practicum.eventservice.category.model.QCategory;
import ru.practicum.eventservice.events.mapper.EventMapper;
import ru.practicum.eventservice.events.model.Event;
import ru.practicum.eventservice.events.model.QEvent;
import ru.practicum.eventservice.events.repository.EventRepository;
import ru.practicum.interaction.dto.event.EventDto;
import ru.practicum.interaction.dto.event.EventShortDto;
import ru.practicum.interaction.dto.user.UserShortDto;
import ru.practicum.interaction.exception.ConflictException;
import ru.practicum.interaction.feign.RequestFeignClient;
import ru.practicum.interaction.feign.UserFeignClient;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public abstract class BaseEventService {

    protected final EventRepository eventRepository;
    protected final EventMapper eventMapper;
    protected final JPAQueryFactory jpaQueryFactory;
    protected final RequestFeignClient requestFeignClient;
    protected final UserFeignClient userFeignClient;

    protected List<EventShortDto> fetchEventsWithStats(BooleanExpression queryExpression, Pageable pageRequest) {
        List<EventShortDto> events = fetchEvents(queryExpression, pageRequest);
        enrichWithStats(events);
        return events;
    }

    protected List<EventShortDto> fetchEvents(BooleanExpression queryExpression, Pageable pageRequest) {
        List<Event> events = jpaQueryFactory
                .selectFrom(QEvent.event)
                .leftJoin(QEvent.event.category, QCategory.category)
                .fetchJoin()
                .where(queryExpression)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .stream()
                .toList();

        List<Long> initiatorIds = events.stream().map(Event::getInitiatorId).toList();
        Map<Long, UserShortDto> initiators = userFeignClient.getUsersByIDS(initiatorIds);

        return events.stream()
                .map(event -> eventMapper.toEventShortDto(event, initiators.get(event.getInitiatorId())))
                .toList();
    }

    protected void enrichWithStats(List<? extends EventDto> events) {
        List<Long> eventIds = events.stream().map(EventDto::getId).toList();
        Map<Long, Long> confirmedRequestsMap = getConfirmedRequests(eventIds);

        events.forEach(event ->
                event.setConfirmedRequests(confirmedRequestsMap.getOrDefault(event.getId(), 0L)));
    }

    protected Map<Long, Long> getConfirmedRequests(List<Long> eventIds) {
        return requestFeignClient.getConfirmedRequests(eventIds);
    }

    protected void validateEventOwnership(Long userId, Event event) {
        UserShortDto initiator = getEventInitiator(event);
        if (!initiator.getId().equals(userId)) {
            throw new ConflictException(String.format(
                    "Пользователь с id = %s не является владельцем события c id = %s",
                    userId, event.getId()
            ));
        }
    }

    protected UserShortDto getEventInitiator(Event event) {
        return userFeignClient.getUserShortById(event.getInitiatorId());
    }
}