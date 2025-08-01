package ru.practicum.eventservice.events.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatRestClient;
import ru.practicum.eventservice.category.mapper.CategoryMapper;
import ru.practicum.eventservice.category.model.Category;
import ru.practicum.eventservice.category.service.CategoryServicePublic;
import ru.practicum.eventservice.events.mapper.EventMapper;
import ru.practicum.eventservice.events.model.Event;
import ru.practicum.eventservice.events.model.QEvent;
import ru.practicum.eventservice.events.repository.EventRepository;
import ru.practicum.eventservice.events.service.BaseEventService;
import ru.practicum.eventservice.events.service.EventServicePrivate;
import ru.practicum.interaction.dto.event.EventFullDto;
import ru.practicum.interaction.dto.event.EventShortDto;
import ru.practicum.interaction.dto.event.NewEventDto;
import ru.practicum.interaction.dto.event.enums.EventState;
import ru.practicum.interaction.dto.event.requests.UpdateEventUserRequest;
import ru.practicum.interaction.dto.user.UserShortDto;
import ru.practicum.interaction.exception.ConflictException;
import ru.practicum.interaction.exception.NotFoundException;
import ru.practicum.interaction.feign.RequestFeignClient;
import ru.practicum.interaction.feign.UserFeignClient;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class EventServicePrivateImpl extends BaseEventService implements EventServicePrivate {

    public EventServicePrivateImpl(EventRepository eventRepository, EventMapper eventMapper,
                                   JPAQueryFactory jpaQueryFactory, StatRestClient statRestClient,
                                   RequestFeignClient requestFeignClient, UserFeignClient userFeignClient,
                                   CategoryServicePublic categoryService, CategoryMapper categoryMapper) {
        super(eventRepository, eventMapper, jpaQueryFactory, statRestClient, requestFeignClient, userFeignClient);
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    private final CategoryServicePublic categoryService;
    private final CategoryMapper categoryMapper;

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с ID=" + eventId + " не найдено"));
        UserShortDto initiator = userFeignClient.getUserShortById(event.getInitiatorId());
        if (!initiator.getId().equals(userId)) {
            throw new ConflictException("Пользователь " + userId + " не является владельцем события " + eventId);
        }
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        eventFullDto.setInitiator(initiator);
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
        log.info("Создано событие пользователем {}", userId);
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
                .orElseThrow(() -> new NotFoundException("Событие с ID=" + eventId + " не найдено"));
        UserShortDto initiator = userFeignClient.getUserShortById(event.getInitiatorId());
        if (!initiator.getId().equals(userId)) {
            throw new ConflictException("Пользователь " + userId + " не является владельцем события " + eventId);
        }
        if (event.getState() != EventState.PENDING && event.getState() != EventState.CANCELED) {
            throw new ConflictException("Можно обновлять только события в состоянии PENDING или CANCELED");
        }
        Category category = categoryMapper.toCategory(categoryService.getByIDCategoryPublic(event.getCategory().getId()));
        Event updatedEvent = eventMapper.toUpdatedEvent(event, updateRequest, category);
        Event savedEvent = eventRepository.save(updatedEvent);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(savedEvent);
        eventFullDto.setInitiator(initiator);
        log.info("Обновлено событие {} пользователем {}", eventId, userId);
        return eventFullDto;
    }
}