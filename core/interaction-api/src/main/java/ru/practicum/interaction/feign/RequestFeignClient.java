package ru.practicum.interaction.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

import static ru.practicum.interaction.util.ConstantsUtil.COUNT_EVENT_STATUS;
import static ru.practicum.interaction.util.ConstantsUtil.REQUESTS;
import static ru.practicum.interaction.util.ConstantsUtil.REQUEST_CONFIRMED;

@FeignClient(name = "request-service", path = REQUESTS)
public interface RequestFeignClient {
    @GetMapping(REQUEST_CONFIRMED)
    Map<Long, Long> getConfirmedRequests(@RequestParam List<Long> eventIds);

    @GetMapping(COUNT_EVENT_STATUS)
    Long countAllByEventIdAndStatus(@PathVariable Long eventId,
                                      @PathVariable String requestStatus);
}