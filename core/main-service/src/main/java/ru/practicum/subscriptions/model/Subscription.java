package ru.practicum.subscriptions.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

/**
 * Entity representing a subscription relationship between users.
 *
 * Fields:
 * - `id` – Unique identifier of the subscription. Auto-generated.
 * - `subscriber` – User who initiated the subscription. Lazy-loaded; cannot be null.
 * - `subscribedTo` – User being followed. Lazy-loaded; cannot be null.
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id", nullable = false)
    User subscriber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscribed_to_id", nullable = false)
    User subscribedTo;

    @Column(name = "created", nullable = false)
    LocalDateTime created = LocalDateTime.now();
}
