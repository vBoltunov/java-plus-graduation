package ru.practicum.requestservice.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.interaction.dto.request.RequestDto;
import ru.practicum.interaction.dto.request.RequestUpdateDto;
import ru.practicum.interaction.dto.request.RequestUpdateResultDto;
import ru.practicum.requestservice.service.RequestService;

import java.util.List;

import static ru.practicum.interaction.util.PathConstants.PRIVATE_EVENT_REQUESTS;
import static ru.practicum.interaction.util.PathConstants.PRIVATE_REQUESTS;
import static ru.practicum.interaction.util.PathConstants.PRIVATE_REQUEST_CANCEL;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
public class RequestControllerPrivate {

    private final RequestService requestService;

    @GetMapping(PRIVATE_REQUESTS)
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> getUserRequests(@PathVariable Long userId) {
        return requestService.getUserRequests(userId);
    }

    @PostMapping(PRIVATE_REQUESTS)
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable Long userId, @NotNull @RequestParam Long eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping(PRIVATE_REQUEST_CANCEL)
    @ResponseStatus(HttpStatus.OK)
    public RequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping(PRIVATE_EVENT_REQUESTS)
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> getEventRequests(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId) {
        return requestService.getEventRequests(userId, eventId);
    }

    @PatchMapping(PRIVATE_EVENT_REQUESTS)
    @ResponseStatus(HttpStatus.OK)
    public RequestUpdateResultDto updateEventRequests(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @RequestBody RequestUpdateDto updateDto) {
        return requestService.updateEventRequests(userId, eventId, updateDto);
    }
}