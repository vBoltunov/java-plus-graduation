package ru.practicum.subscriptionservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.subscriptionservice.model.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Page<Subscription> findBySubscriberId(Long subscribedToId, Pageable pageable);

    int deleteBySubscribedToId(Long subscribedId);
}
