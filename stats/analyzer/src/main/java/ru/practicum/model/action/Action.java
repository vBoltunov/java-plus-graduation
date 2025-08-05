package ru.practicum.model.action;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

/**
 * Entity representing a user action on an event in the system.
 *
 * Fields:
 * - `id` – Unique identifier of the action. Auto-generated.
 * - `userId` – ID of the user who performed the action.
 * - `eventId` – ID of the event on which the action was performed.
 * - `actionType` – Type of the action performed (enum value).
 * - `timestamp` – Timestamp when the action occurred.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "actions")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long userId;

    Long eventId;

    @Enumerated(EnumType.STRING)
    ActionType actionType;

    Instant timestamp;
}
