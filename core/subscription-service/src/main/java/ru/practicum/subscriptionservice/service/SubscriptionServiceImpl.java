package ru.practicum.subscriptionservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.interaction.dto.subscription.SubscriptionDto;
import ru.practicum.interaction.dto.user.UserShortDto;
import ru.practicum.interaction.exception.ConflictException;
import ru.practicum.interaction.exception.NotFoundException;
import ru.practicum.interaction.feign.UserFeignClient;
import ru.practicum.subscriptionservice.mapper.SubscriptionMapper;
import ru.practicum.subscriptionservice.model.Subscription;
import ru.practicum.subscriptionservice.repository.SubscriptionRepository;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserFeignClient userFeignClient;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    @Transactional
    public SubscriptionDto subscribe(Long subscriberId, Long subscribedToId) {
        if (subscriberId.equals(subscribedToId)) {
            throw new ConflictException("Пользователь не может подписаться сам на себя");
        }

        Subscription subscription = subscriptionRepository.save(
                subscriptionMapper.toSubscription(new Subscription(), subscriberId, subscribedToId)
        );
        log.info("Подписка оформлена");

        SubscriptionDto subscriptionDto = subscriptionMapper.toSubscriptionShortDto(subscription);

        try {
            subscriptionDto.setSubscriber(userFeignClient.getUserShortById(subscriberId));
            subscriptionDto.setSubscribedTo(userFeignClient.getUserShortById(subscribedToId));
        } catch (NoSuchElementException e) {
            throw new NotFoundException(String.format("Пользователь с id = %s не найден", subscribedToId));
        }

        return subscriptionDto;
    }

    @Override
    @Transactional
    public void unsubscribe(Long subscriberId, Long subscribedToId) {
        int deletedCount = subscriptionRepository.deleteBySubscribedToId(subscribedToId);
        if (deletedCount == 0) {
            throw new NotFoundException("Подписка не найдена");
        }
    }

    @Override
    public Set<UserShortDto> getSubscriptions(Long userId, Pageable page) {
        Page<Subscription> subscribed = subscriptionRepository.findBySubscriberId(userId, page);
        List<Long> subscribedIds = subscribed.get()
                .map(Subscription::getSubscribedToId)
                .toList();

        return new HashSet<>(userFeignClient.getUsersByIDS(subscribedIds).values());
    }
}
