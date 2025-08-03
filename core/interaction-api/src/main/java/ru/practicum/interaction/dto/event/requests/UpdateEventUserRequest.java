package ru.practicum.interaction.dto.event.requests;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.interaction.dto.event.enums.UpdateUserStateAction;
import ru.practicum.interaction.dto.event.validators.EventDateInTwoHours;

import java.time.LocalDateTime;

/**
 * Request DTO for event updates initiated by users.
 *
 * Fields:
 * - `stateAction` – User-initiated state change:
 *   SEND_TO_REVIEW (submit for moderation), CANCEL_REVIEW (revert to draft).
 * - `eventDate` – New event date/time. Must be at least 2 hours in the future.
 *
 * Inherits all updatable fields from {@link UpdateEventRequest}:
 * - annotation, category, description, location, paid, participantLimit,
 *   requestModeration, title.
 */
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventUserRequest extends UpdateEventRequest {
    UpdateUserStateAction stateAction;

    @EventDateInTwoHours
    LocalDateTime eventDate;
}