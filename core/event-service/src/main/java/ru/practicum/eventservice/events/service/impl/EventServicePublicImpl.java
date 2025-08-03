package ru.practicum.eventservice.events.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatRestClient;
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
import ru.practicum.interaction.feign.RequestFeignClient;
import ru.practicum.interaction.feign.UserFeignClient;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class EventServicePublicImpl extends BaseEventService implements EventServicePublic {

    public EventServicePublicImpl(EventRepository eventRepository, EventMapper eventMapper,
                                  JPAQueryFactory jpaQueryFactory, StatRestClient statRestClient,
                                  RequestFeignClient requestFeignClient, UserFeignClient userFeignClient) {
        super(eventRepository, eventMapper, jpaQueryFactory, statRestClient, requestFeignClient, userFeignClient);
    }

    private static final int TIME_BEFORE = 10;

    @Override
    public List<EventShortDto> getEventsByParams(EventPublicFilterRequest filterCriteria, Pageable pageRequest) {
        BooleanExpression queryExpression = buildPublicFilterExpression(filterCriteria);
        List<EventShortDto> events = fetchEventsWithStats(queryExpression, pageRequest);
        log.info("Получены публичные события по параметрам");
        return events;
    }

    @Override
    public EventFullDto getEventById(Long eventId) {
        EventFullDto event = eventRepository.findById(eventId)
                .map(eventMapper::toEventFullDto)
                .filter(e -> e.getState() == EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(String.format("Событие с id = %s не найдено", eventId)));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusYears(TIME_BEFORE);
        statRestClient.getStats(start, now, List.of("/events/" + eventId), true)
                .forEach(viewStatsDto -> event.setViews(viewStatsDto.getHits()));
        long confirmedRequests = requestFeignClient.countAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED.toString());
        event.setConfirmedRequests(confirmedRequests);
        log.info("Получено событие {} по ID", eventId);
        return event;
    }

    @Override
    public void recordHit(String uri, String ip) {
        super.recordHit(uri, ip);
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