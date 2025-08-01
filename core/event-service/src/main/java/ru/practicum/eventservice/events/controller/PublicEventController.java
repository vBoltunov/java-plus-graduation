package ru.practicum.eventservice.events.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventservice.events.service.EventServicePublic;
import ru.practicum.interaction.dto.event.EventFullDto;
import ru.practicum.interaction.dto.event.EventShortDto;
import ru.practicum.interaction.dto.event.requests.EventPublicFilterRequest;

import java.util.List;

import static ru.practicum.interaction.util.PathConstants.EVENTS;
import static ru.practicum.interaction.util.PathConstants.EVENT_ID;

@Slf4j
@RestController
@RequestMapping(EVENTS)
@RequiredArgsConstructor
public class PublicEventController {

    private final EventServicePublic eventServicePublic;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsByParams(@Valid @ModelAttribute EventPublicFilterRequest filterCriteria,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 HttpServletRequest request) {
        log.info("GET /events - Запрос публичных событий по критериям: {}, from={}, size={}", filterCriteria, from, size);
        List<EventShortDto> events = eventServicePublic.getEventsByParams(filterCriteria, PageRequest.of(from / size, size));
        eventServicePublic.recordHit("/events", request.getRemoteAddr());
        return events;
    }

    @GetMapping(EVENT_ID)
    public ResponseEntity<EventFullDto> getEventById(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("GET /events/{} - Запрос события по ID: {}", eventId, eventId);
        EventFullDto event = eventServicePublic.getEventById(eventId);
        eventServicePublic.recordHit("/events/" + eventId, request.getRemoteAddr());
        return ResponseEntity.ok(event);
    }
}