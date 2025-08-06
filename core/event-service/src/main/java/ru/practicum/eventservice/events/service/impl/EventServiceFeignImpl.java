package ru.practicum.eventservice.events.service.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.eventservice.events.mapper.EventMapper;
import ru.practicum.eventservice.events.model.Event;
import ru.practicum.eventservice.events.repository.EventRepository;
import ru.practicum.eventservice.events.service.BaseEventService;
import ru.practicum.eventservice.events.service.EventServiceFeign;
import ru.practicum.interaction.dto.event.EventFullDto;
import ru.practicum.interaction.exception.NotFoundException;
import ru.practicum.interaction.feign.RequestFeignClient;
import ru.practicum.interaction.feign.UserFeignClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class EventServiceFeignImpl extends BaseEventService implements EventServiceFeign {

    public EventServiceFeignImpl(EventRepository eventRepository, EventMapper eventMapper,
                                 JPAQueryFactory jpaQueryFactory,
                                 RequestFeignClient requestFeignClient, UserFeignClient userFeignClient) {
        super(eventRepository, eventMapper, jpaQueryFactory, requestFeignClient, userFeignClient);
    }

    @Override
    public EventFullDto getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Событие с id = %s не найдено", eventId)));
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        eventFullDto.setInitiator(getEventInitiator(event));
        log.info("Получено событие по ID {}", eventId);
        return eventFullDto;
    }

    @Override
    public EventFullDto getEventByUserId(Long eventId, Long userId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        if (event == null) {
            throw new NotFoundException(String.format("Событие с id = %s не найдено для пользователя с id = %s",
                    eventId, userId));
        }
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        eventFullDto.setInitiator(userFeignClient.getUserShortById(userId));
        log.info("Получено событие {} для пользователя {}", eventId, userId);
        return eventFullDto;
    }
}