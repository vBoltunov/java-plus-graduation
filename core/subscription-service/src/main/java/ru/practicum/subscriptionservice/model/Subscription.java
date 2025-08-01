package ru.practicum.subscriptionservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * Entity representing a subscription relationship between users.
 *
 * Fields:
 * - `id` – Unique identifier of the subscription. Auto-generated.
 * - `subscriberId` – ID of the user who initiated the subscription..
 * - `subscribedToId` – ID of the user being followed.
 * - `created` – Timestamp of subscription creation. Defaults to current time.
 */
@Getter
@Setter
@Entity
@Table(name = "subscriptions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JoinColumn(name = "subscriber_id", nullable = false)
    Long subscriberId;

    @JoinColumn(name = "subscribed_to_id", nullable = false)
    Long subscribedToId;

    @Column(name = "created", nullable = false)
    LocalDateTime created = LocalDateTime.now();
}
