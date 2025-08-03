package ru.practicum.eventservice.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventservice.events.service.EventServicePrivate;
import ru.practicum.interaction.dto.event.EventFullDto;
import ru.practicum.interaction.dto.event.EventShortDto;
import ru.practicum.interaction.dto.event.NewEventDto;
import ru.practicum.interaction.dto.event.requests.UpdateEventUserRequest;

import java.util.List;

import static ru.practicum.interaction.util.ConstantsUtil.PRIVATE_EVENTS;
import static ru.practicum.interaction.util.ConstantsUtil.EVENT_ID;

@Slf4j
@RestController
@RequestMapping(PRIVATE_EVENTS)
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventServicePrivate eventServicePrivate;

    @GetMapping(EVENT_ID)
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("GET /users/{}/events/{} - Запрос события для пользователя: {}, ID: {}", userId, eventId, userId, eventId);
        return ResponseEntity.ok(eventServicePrivate.getEvent(userId, eventId));
    }

    @PostMapping
    public ResponseEntity<EventFullDto> addEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {
        log.info("POST /users/{}/events - Создание события пользователем: {}, данные: {}", userId, userId, newEventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventServicePrivate.addEvent(userId, newEventDto));
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(@PathVariable Long userId,
                                                         @RequestParam(defaultValue = "0") int from,
                                                         @RequestParam(defaultValue = "10") int size) {
        log.info("GET /users/{}/events - Запрос событий пользователя: {}, from={}, size={}", userId, userId, from, size);
        return ResponseEntity.ok(eventServicePrivate.getEvents(userId, PageRequest.of(from / size, size)));
    }

    @PatchMapping(EVENT_ID)
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable Long userId,
                                                    @PathVariable Long eventId,
                                                    @Valid @RequestBody UpdateEventUserRequest updateRequest) {
        log.info("PATCH /users/{}/events/{} - Обновление события пользователем: {}, ID: {}", userId, eventId, userId, eventId);
        return ResponseEntity.ok(eventServicePrivate.updateEvent(userId, eventId, updateRequest));
    }
}