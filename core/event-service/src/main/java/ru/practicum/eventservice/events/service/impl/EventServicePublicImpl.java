package ru.practicum.eventservice.events.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ru.practicum.eventservice.events.mapper.EventMapper;
import ru.practicum.eventservice.events.model.QEvent;
import ru.practicum.eventservice.events.repository.EventRepository;
import ru.practicum.eventservice.events.service.BaseEventService;
import ru.practicum.eventservice.events.service.EventServicePublic;
import ru.practicum.interaction.dto.event.EventFullDto;
import ru.practicum.interaction.dto.event.EventShortDto;
import ru.practicum.interaction.dto.event.enums.EventState;
import ru.practicum.interaction.dto.event.requests.EventPublicFilterRequest;
import ru.practicum.interaction.dto.request.enums.RequestStatus;
import ru.practicum.interaction.exception.NotFoundException;
import ru.practicum.interaction.exception.ValidationException;
import ru.practicum.interaction.feign.RequestFeignClient;
import ru.practicum.interaction.feign.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.AnalyzerClient;
import ru.practicum.client.CollectorClient;
import ru.practicum.grpc.stats.recommendation.RecommendedEventProto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class EventServicePublicImpl extends BaseEventService implements EventServicePublic {

    public EventServicePublicImpl(EventRepository eventRepository, EventMapper eventMapper,
                                  JPAQueryFactory jpaQueryFactory,
                                  RequestFeignClient requestFeignClient, UserFeignClient userFeignClient, CollectorClient collectorClient, AnalyzerClient analyzerClient) {
        super(eventRepository, eventMapper, jpaQueryFactory, requestFeignClient, userFeignClient);
        this.collectorClient = collectorClient;
        this.analyzerClient = analyzerClient;
    }

    final CollectorClient collectorClient;
    private final AnalyzerClient analyzerClient;

    @Override
    public List<EventShortDto> getEventsByParams(EventPublicFilterRequest filterCriteria, Pageable pageRequest) {
        BooleanExpression queryExpression = buildPublicFilterExpression(filterCriteria);
        List<EventShortDto> events = fetchEventsWithStats(queryExpression, pageRequest);
        log.info("Получены публичные события по параметрам");
        return events;
    }

    @Override
    public EventFullDto getEventById(Long eventId, Long userId) {
        EventFullDto event = eventRepository.findById(eventId).map(eventMapper::toEventFullDto)
                .orElseThrow(() -> new NotFoundException(String.format("Событие с id = %s не найдено", eventId)));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException(String.format("Событие с id = %s не опубликовано", eventId));
        }

        long confirmedRequests = requestFeignClient.countAllByEventIdAndStatus(eventId,
                RequestStatus.CONFIRMED.toString());
        event.setConfirmedRequests(confirmedRequests);
        log.info("Получено событие с id = {}", eventId);
        return event;
    }

    @Override
    public List<EventFullDto> getRecommendations(Long userId, int maxResults) {
        Set<Long> eventIds = analyzerClient.getRecommendations(userId, maxResults)
                .map(RecommendedEventProto::getEventId)
                .collect(Collectors.toSet());

        return eventRepository
                .findAllByIdIn(eventIds)
                .stream()
                .map(eventMapper::toEventFullDto)
                .toList();
    }

    @Override
    public void like(Long eventId, Long userId) {
        if (requestFeignClient.isRequestExist(eventId, userId)) {
            collectorClient.addLikeEvent(
                    userId,
                    eventId);
        } else {
            throw new ValidationException("Нельзя поставить лайк. Пользователь не подавал заявку на участие");
        }
    }

    private BooleanExpression buildPublicFilterExpression(EventPublicFilterRequest filterCriteria) {
        BooleanExpression expression = QEvent.event.state.eq(EventState.PUBLISHED);
        if (filterCriteria.getRangeStart() != null) {
            expression = expression.and(QEvent.event.eventDate.after(filterCriteria.getRangeStart()));
        }
        if (filterCriteria.getRangeEnd() != null) {
            expression = expression.and(QEvent.event.eventDate.before(filterCriteria.getRangeEnd()));
        }
        if (filterCriteria.getPaid() != null) {
            expression = expression.and(QEvent.event.paid.eq(filterCriteria.getPaid()));
        }
        if (filterCriteria.getCategories() != null && !filterCriteria.getCategories().isEmpty()) {
            expression = expression.and(QEvent.event.category.id.in(filterCriteria.getCategories()));
        }
        if (filterCriteria.getText() != null && !filterCriteria.getText().isEmpty()) {
            expression = expression.and(QEvent.event.annotation.containsIgnoreCase(filterCriteria.getText())
                    .or(QEvent.event.description.containsIgnoreCase(filterCriteria.getText())));
        }
        return expression;
    }
}