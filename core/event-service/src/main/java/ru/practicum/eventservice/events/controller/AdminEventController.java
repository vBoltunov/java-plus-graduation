package ru.practicum.eventservice.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventservice.events.service.EventServiceAdmin;
import ru.practicum.interaction.dto.event.EventFullDto;
import ru.practicum.interaction.dto.event.requests.EventAdminFilterRequest;
import ru.practicum.interaction.dto.event.requests.UpdateEventAdminRequest;

import java.util.List;

import static ru.practicum.interaction.util.PathConstants.ADMIN_EVENTS;
import static ru.practicum.interaction.util.PathConstants.EVENT_ID;

@Slf4j
@RestController
@RequestMapping(ADMIN_EVENTS)
@RequiredArgsConstructor
public class AdminEventController {

    private final EventServiceAdmin eventServiceAdmin;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEventsByFilter(@Validated @ModelAttribute EventAdminFilterRequest filterCriteria,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("GET /admin/events - Запрос на получение событий по критериям: {}, from={}, size={}", filterCriteria, from, size);
        return eventServiceAdmin.getEventsByParams(filterCriteria, PageRequest.of(from / size, size));
    }

    @PatchMapping(EVENT_ID)
    public ResponseEntity<EventFullDto> updateEventById(@PathVariable("eventId") Long eventId,
                                                        @Valid @RequestBody UpdateEventAdminRequest dto) {
        log.info("PATCH /admin/events/{} - Обновление события админом, ID: {}, данные: {}", eventId, eventId, dto);
        return ResponseEntity.ok(eventServiceAdmin.updateEventById(eventId, dto));
    }
}