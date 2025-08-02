package ru.practicum.subscriptionservice.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.interaction.dto.subscription.SubscriptionDto;
import ru.practicum.interaction.dto.user.UserShortDto;

import java.util.Set;

public interface SubscriptionService {
    SubscriptionDto subscribe(Long subscriberId, Long subscribedToId);

    void unsubscribe(Long subscriberId, Long subscribedToId);

    Set<UserShortDto> getSubscriptions(Long userId, Pageable page);
}
