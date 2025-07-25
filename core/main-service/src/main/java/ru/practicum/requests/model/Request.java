package ru.practicum.requests.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import ru.practicum.events.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

/**
 * Entity representing a participation request for an event.
 *
 * Fields:
 * - `id` – Unique identifier of the request. Auto-generated.
 * - `created` – Timestamp when the request was submitted.
 * - `event` – Event for which the participation was requested.
 * - `requester` – User who submitted the request.
 * - `status` – Current status of the request (e.g., PENDING, CONFIRMED, REJECTED).
 */
@Entity
@Table(name = "requests")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "created", nullable = false)
    LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    Event event;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    User requester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    RequestStatus status;
}
