package ru.practicum.interaction.dto.event.requests;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.interaction.dto.event.enums.UpdateAdminStateAction;
import ru.practicum.interaction.dto.event.validators.EventDateInOneHour;

import java.time.LocalDateTime;

/**
 * Admin request for updating event details with administrative controls.
 *
 * Fields:
 * - `stateAction` – Administrative action to change event state:
 *   PUBLISH_EVENT, REJECT_EVENT (required for state transitions).
 * - `eventDate` – New date/time for the event. Must be at least 1 hour in the future.
 *
 * Inherits all updatable fields from {@link UpdateEventRequest}.
 */
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventAdminRequest extends UpdateEventRequest {
    UpdateAdminStateAction stateAction;

    @EventDateInOneHour
    LocalDateTime eventDate;
}