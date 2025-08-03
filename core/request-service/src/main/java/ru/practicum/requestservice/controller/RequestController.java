package ru.practicum.requestservice.controller;

import ru.practicum.interaction.feign.RequestFeignClient;
import ru.practicum.requestservice.service.RequestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static ru.practicum.interaction.util.ConstantsUtil.COUNT_EVENT_STATUS;
import static ru.practicum.interaction.util.ConstantsUtil.REQUESTS;
import static ru.practicum.interaction.util.ConstantsUtil.REQUEST_CONFIRMED;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping(REQUESTS)
public class RequestController implements RequestFeignClient {
    private final RequestService requestService;

    @GetMapping(REQUEST_CONFIRMED)
    public Map<Long, Long> getConfirmedRequests(@RequestParam List<Long> eventIds) {
        return requestService.getConfirmedRequests(eventIds);
    }

    @GetMapping(COUNT_EVENT_STATUS)
    public Long countAllByEventIdAndStatus(@PathVariable Long eventId,
                                           @PathVariable String requestStatus) {
        return requestService.countAllByEventIdAndStatus(eventId, requestStatus);
    }
}