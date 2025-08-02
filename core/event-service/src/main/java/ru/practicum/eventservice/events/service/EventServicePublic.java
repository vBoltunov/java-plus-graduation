package ru.practicum.eventservice.events.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.interaction.dto.event.EventFullDto;
import ru.practicum.interaction.dto.event.EventShortDto;
import ru.practicum.interaction.dto.event.requests.EventPublicFilterRequest;

import java.util.List;

public interface EventServicePublic {
    List<EventShortDto> getEventsByParams(EventPublicFilterRequest filterCriteria, Pageable pageRequest);

    EventFullDto getEventById(Long eventId);

    void recordHit(String uri, String ip);
}