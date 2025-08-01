package ru.practicum.eventservice.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventservice.events.service.EventServiceFeign;
import ru.practicum.interaction.dto.event.EventFullDto;
import ru.practicum.interaction.feign.EventFeignClient;

import static ru.practicum.interaction.util.PathConstants.EVENTS_FEIGN;
import static ru.practicum.interaction.util.PathConstants.EVENT_ID;
import static ru.practicum.interaction.util.PathConstants.EVENT_ID_USER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(EVENTS_FEIGN)
public class FeignEventController implements EventFeignClient {

    private final EventServiceFeign eventServiceFeign;

    @GetMapping(EVENT_ID_USER_ID)
    public EventFullDto getEventByUserId(@PathVariable Long eventId, @PathVariable Long userId) {
        log.info("GET /events/feign/{}/users/{} - Запрос события для пользователя: {}, ID: {}",
                eventId, userId, userId, eventId);
        return eventServiceFeign.getEventByUserId(eventId, userId);
    }

    @GetMapping(EVENT_ID)
    @Override
    public EventFullDto getEventById(@PathVariable Long eventId) {
        log.info("GET /events/feign/{} - Запрос события по ID: {}", eventId, eventId);
        return eventServiceFeign.getEventById(eventId);
    }
}