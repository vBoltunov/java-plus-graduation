package ru.practicum.eventservice.events.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatRestClient;
import ru.practicum.eventservice.events.mapper.EventMapper;
import ru.practicum.eventservice.events.model.Event;
import ru.practicum.eventservice.events.model.QEvent;
import ru.practicum.eventservice.events.repository.EventRepository;
import ru.practicum.eventservice.events.service.BaseEventService;
import ru.practicum.eventservice.events.service.EventServiceAdmin;
import ru.practicum.interaction.dto.event.EventFullDto;
import ru.practicum.interaction.dto.event.EventShortDto;
import ru.practicum.interaction.dto.event.enums.EventState;
import ru.practicum.interaction.dto.event.requests.EventAdminFilterRequest;
import ru.practicum.interaction.dto.event.requests.UpdateEventAdminRequest;
import ru.practicum.interaction.exception.ConflictException;
import ru.practicum.interaction.exception.NotFoundException;
import ru.practicum.interaction.feign.RequestFeignClient;
import ru.practicum.interaction.feign.UserFeignClient;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class EventServiceAdminImpl extends BaseEventService implements EventServiceAdmin {

    public EventServiceAdminImpl(EventRepository eventRepository, EventMapper eventMapper,
                                 JPAQueryFactory jpaQueryFactory, StatRestClient statRestClient,
                                 RequestFeignClient requestFeignClient, UserFeignClient userFeignClient) {
        super(eventRepository, eventMapper, jpaQueryFactory, statRestClient, requestFeignClient, userFeignClient);
    }

    @Override
    public List<EventFullDto> getEventsByParams(EventAdminFilterRequest filterCriteria, Pageable pageRequest) {
        BooleanExpression queryExpression = buildAdminFilterExpression(filterCriteria);
        List<EventShortDto> events = fetchEvents(queryExpression, pageRequest);
        List<EventFullDto> fullEvents = events.stream()
                .map(event -> eventMapper.toEventFullDto(eventRepository.findById(event.getId()).orElseThrow()))
                .toList();
        enrichWithStats(fullEvents);
        log.info("Получены события для админа по параметрам");
        return fullEvents;
    }

    @Override
    @Transactional
    public EventFullDto updateEventById(Long eventId, UpdateEventAdminRequest dto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с ID=" + eventId + " не найдено"));
        if (event.getState().equals(EventState.PUBLISHED) || event.getState().equals(EventState.CANCELED)) {
            throw new ConflictException("Админ может обновлять только события в состоянии PENDING");
        }
        eventRepository.save(eventMapper.toUpdatedEvent(event, dto, event.getCategory()));
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        log.info("Обновлено событие {} админом", eventId);
        return eventFullDto;
    }

    private BooleanExpression buildAdminFilterExpression(EventAdminFilterRequest filterCriteria) {
        BooleanExpression expression = QEvent.event.isNotNull(); // Базовое выражение
        QEvent qEvent = QEvent.event;

        if (filterCriteria.getUserIds() != null && !filterCriteria.getUserIds().isEmpty()) {
            expression = expression.and(qEvent.initiatorId.in(filterCriteria.getUserIds()));
        }
        if (filterCriteria.getStates() != null && !filterCriteria.getStates().isEmpty()) {
            expression = expression.and(qEvent.state.in(filterCriteria.getStates()));
        }
        if (filterCriteria.getCategories() != null && !filterCriteria.getCategories().isEmpty()) {
            expression = expression.and(qEvent.category.id.in(filterCriteria.getCategories()));
        }
        if (filterCriteria.getRangeStart() != null) {
            expression = expression.and(qEvent.eventDate.after(filterCriteria.getRangeStart()));
        }
        if (filterCriteria.getRangeEnd() != null) {
            expression = expression.and(qEvent.eventDate.before(filterCriteria.getRangeEnd()));
        }

        return expression;
    }
}