package ru.practicum.eventservice.events.service;

import ru.practicum.interaction.dto.event.EventFullDto;

public interface EventServiceFeign {
    EventFullDto getEventByUserId(Long eventId, Long userId);

    EventFullDto getEventById(Long eventId);

}
