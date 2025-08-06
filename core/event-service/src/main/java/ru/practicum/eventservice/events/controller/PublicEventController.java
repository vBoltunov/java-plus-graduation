package ru.practicum.eventservice.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventservice.events.service.EventServicePublic;
import ru.practicum.interaction.dto.event.EventFullDto;
import ru.practicum.interaction.dto.event.EventShortDto;
import ru.practicum.interaction.dto.event.requests.EventPublicFilterRequest;

import java.util.List;

import static ru.practicum.interaction.util.ConstantsUtil.EVENTS;
import static ru.practicum.interaction.util.ConstantsUtil.EVENT_ID;
import static ru.practicum.interaction.util.ConstantsUtil.EVENT_LIKE;
import static ru.practicum.interaction.util.ConstantsUtil.RECOMMENDATIONS;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(EVENTS)
public class PublicEventController {

    private final EventServicePublic eventServicePublic;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsByParams(@Valid @ModelAttribute EventPublicFilterRequest filterCriteria,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        log.info("GET /events - Запрос публичных событий по критериям: {}, from={}, size={}", filterCriteria, from, size);
        return eventServicePublic.getEventsByParams(filterCriteria, PageRequest.of(from / size, size));
    }

    @GetMapping(EVENT_ID)
    public EventFullDto getEventById(@PathVariable Long eventId, @RequestHeader("X-EWM-USER-ID") Long userId) {
        log.info("GET /events/{} - Запрос события по ID: {}", eventId, eventId);
        return eventServicePublic.getEventById(eventId, userId);
    }

    @GetMapping(RECOMMENDATIONS)
    List<EventFullDto> getRecommendations(@RequestHeader("X-EWM-USER-ID") Long userId,
                                          @RequestParam("maxResults") int maxResults) {
        log.info("GET /events/recommendations - Запрос рекомендаций для пользователя с ID: {}", userId);
        return eventServicePublic.getRecommendations(userId, maxResults);
    }

    @PutMapping(EVENT_LIKE)
    void like(@PathVariable Long eventId,
              @RequestHeader("X-EWM-USER-ID") Long userId) {
        log.info("PUT /events/{}/like - Лайк от пользователя {} событию {}", eventId, userId, eventId);
        eventServicePublic.like(eventId, userId);
    }
}