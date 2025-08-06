package ru.practicum.eventservice.events.service;

import ru.practicum.interaction.dto.event.EventFullDto;
import ru.practicum.interaction.dto.event.EventShortDto;
import org.springframework.data.domain.Pageable;
import ru.practicum.interaction.dto.event.requests.EventPublicFilterRequest;

import java.util.List;

public interface EventServicePublic {
    List<EventShortDto> getEventsByParams(EventPublicFilterRequest params, Pageable pageRequest);

    EventFullDto getEventById(Long eventId, Long userId);

    List<EventFullDto> getRecommendations(Long userId, int maxResults);

    void like(Long eventId, Long userId);
}