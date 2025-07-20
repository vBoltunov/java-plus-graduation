package ru.practicum.events.dto;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO for updating an event by an admin.
 *
 * Fields:
 * - `title` – Updated event title. Length: 3–120 characters.
 * - `annotation` – Updated short description. Length: 20–2000 characters.
 * - `description` – Updated full description. Length: 20–7000 characters.
 * - `category` – Updated category ID.
 * - `location` – Updated event location.
 * - `eventDate` – New scheduled time.
 * - `paid` – Updated paid status.
 * - `participantLimit` – Updated participant limit. Must be zero or positive.
 * - `requestModeration` – Updated moderation flag.
 * - `stateAction` – Admin-specific state update action.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventAdminRequestDto {
    @Size(min = 3, max = 120)
    String title;

    @Size(min = 20, max = 2000)
    String annotation;

    @Size(min = 20, max = 7000)
    String description;

    Long category;

    LocationDto location;
    String eventDate;

    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;

    private UpdateAdminStateAction stateAction;
}