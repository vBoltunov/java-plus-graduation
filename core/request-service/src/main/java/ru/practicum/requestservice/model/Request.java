package ru.practicum.requestservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.interaction.dto.request.enums.RequestStatus;

import java.time.LocalDateTime;

/**
 * Entity representing a participation request for an event.
 *
 * Fields:
 * - `id` – Unique identifier of the request. Auto-generated.
 * - `created` – Timestamp when the request was submitted.
 * - `eventId` – ID of the event for which the participation was requested.
 * - `requesterId` – ID of the user who submitted the request.
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

    @Column(name = "created")
    LocalDateTime created;

    @Column(name = "event_id")
    Long eventId;

    @Column(name = "requester_id")
    Long requesterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 100)
    RequestStatus status = RequestStatus.PENDING;
}
