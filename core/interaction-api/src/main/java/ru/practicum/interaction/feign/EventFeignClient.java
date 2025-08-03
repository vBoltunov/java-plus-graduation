package ru.practicum.interaction.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.interaction.dto.event.EventFullDto;

import static ru.practicum.interaction.util.ConstantsUtil.EVENTS_FEIGN;
import static ru.practicum.interaction.util.ConstantsUtil.EVENT_ID;
import static ru.practicum.interaction.util.ConstantsUtil.EVENT_ID_USER_ID;

@FeignClient(name = "event-service", path = EVENTS_FEIGN)
public interface EventFeignClient {
    @GetMapping(EVENT_ID_USER_ID)
    EventFullDto getEventByUserId(@PathVariable Long userId, @PathVariable Long eventId);

    @GetMapping(EVENT_ID)
    EventFullDto getEventById(@PathVariable Long eventId);
}