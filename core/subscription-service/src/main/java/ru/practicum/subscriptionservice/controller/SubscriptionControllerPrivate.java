package ru.practicum.subscriptionservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.interaction.dto.subscription.SubscriptionDto;
import ru.practicum.interaction.dto.user.UserShortDto;
import ru.practicum.subscriptionservice.service.SubscriptionService;

import java.util.Set;

import static ru.practicum.interaction.util.PathConstants.PATH_TO_SUBSCRIPTION;
import static ru.practicum.interaction.util.PathConstants.PATH_TO_USER;
import static ru.practicum.interaction.util.PathConstants.SUBSCRIBERS;
import static ru.practicum.interaction.util.PathConstants.SUBSCRIPTIONS;

@RestController
@RequestMapping(PATH_TO_USER)
@RequiredArgsConstructor
public class SubscriptionControllerPrivate {
    private final SubscriptionService subscriptionService;

    @PostMapping(PATH_TO_SUBSCRIPTION)
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionDto subscribe(@PathVariable Long userId, @PathVariable Long subscribedToId) {
        return subscriptionService.subscribe(userId, subscribedToId);
    }

    @DeleteMapping(PATH_TO_SUBSCRIPTION)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribe(@PathVariable Long userId, @PathVariable Long subscribedToId) {
        subscriptionService.unsubscribe(userId, subscribedToId);
    }

    @GetMapping(SUBSCRIPTIONS)
    public Set<UserShortDto> getSubscriptions(@PathVariable Long userId,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) {
        return subscriptionService.getSubscriptions(userId, PageRequest.of(from, size));
    }

    @GetMapping(SUBSCRIBERS)
    public Set<UserShortDto> getSubscribers(@PathVariable Long userId,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        return subscriptionService.getSubscriptions(userId, PageRequest.of(from, size));
    }
}
