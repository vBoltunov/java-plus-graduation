package ru.practicum.eventservice.events.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ru.practicum.eventservice.category.mapper.CategoryMapper;
import ru.practicum.eventservice.category.model.Category;
import ru.practicum.eventservice.category.service.CategoryServicePublic;
import ru.practicum.eventservice.events.mapper.EventMapper;
import ru.practicum.eventservice.events.model.Event;
import ru.practicum.eventservice.events.model.QEvent;
import ru.practicum.eventservice.events.repository.EventRepository;
import ru.practicum.eventservice.events.service.BaseEventService;
import ru.practicum.eventservice.events.service.EventServicePrivate;
import ru.practicum.interaction.dto.event.*;
import ru.practicum.interaction.dto.event.enums.EventState;
import ru.practicum.interaction.dto.event.requests.UpdateEventUserRequest;
import ru.practicum.interaction.dto.user.UserShortDto;
import ru.practicum.interaction.exception.ConflictException;
import ru.practicum.interaction.exception.NotFoundException;
import ru.practicum.interaction.feign.RequestFeignClient;
import ru.practicum.interaction.feign.UserFeignClient;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventServicePrivateImpl extends BaseEventService implements EventServicePrivate {

    public EventServicePrivateImpl(EventRepository eventRepository, EventMapper eventMapper,
                                   JPAQueryFactory jpaQueryFactory,
                                   RequestFeignClient requestFeignClient, UserFeignClient userFeignClient,
                                   CategoryServicePublic categoryService, CategoryMapper categoryMapper) {
        super(eventRepository, eventMapper, jpaQueryFactory, requestFeignClient, userFeignClient);
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    private final CategoryServicePublic categoryService;
    private final CategoryMapper categoryMapper;

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Событие с id = %s не найдено", eventId)));
        validateEventOwnership(userId, event);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        eventFullDto.setInitiator(getEventInitiator(event));
        log.info("Получено событие {} для пользователя {}", eventId, userId);
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        UserShortDto initiator = userFeignClient.getUserShortById(userId);
        Category category = categoryMapper.toCategory(categoryService.getByIDCategoryPublic(newEventDto.getCategory()));
        Event event = eventMapper.toEvent(newEventDto, initiator, category);
        eventRepository.save(event);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        eventFullDto.setInitiator(initiator);

        log.info("Создано событие {} пользователем с id {}", eventFullDto, userId);
        return eventFullDto;
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, Pageable pageRequest) {
        BooleanExpression queryExpression = QEvent.event.initiatorId.eq(userId);
        List<EventShortDto> events = fetchEventsWithStats(queryExpression, pageRequest);
        log.info("Получены события для пользователя {}", userId);
        return events;
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Событие с id = %s не найдено", eventId)));
        validateEventOwnership(userId, event);
        if (event.getState() != EventState.PENDING && event.getState() != EventState.CANCELED) {
            throw new ConflictException("Можно обновлять только события в состоянии PENDING или CANCELED");
        }
        Category category = categoryMapper.toCategory(categoryService.getByIDCategoryPublic(event.getCategory().getId()));
        Event updatedEvent = eventMapper.toUpdatedEvent(event, updateRequest, category);
        Event savedEvent = eventRepository.save(updatedEvent);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(savedEvent);
        eventFullDto.setInitiator(getEventInitiator(event));
        log.info("Обновлено событие {} пользователем {}", eventId, userId);
        return eventFullDto;
    }
}