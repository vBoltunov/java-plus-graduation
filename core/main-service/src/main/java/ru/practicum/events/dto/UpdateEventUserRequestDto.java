package ru.practicum.events.dto;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO for updating an event by a user.
 *
 * Fields:
 * - `title` – Updated event title. Length: 3–120 characters.
 * - `annotation` – Updated short description. Length: 20–2000 characters.
 * - `description` – Updated full description. Length: 20–7000 characters.
 * - `category` – Updated category ID.
 * - `location` – Updated location data.
 * - `eventDate` – New date/time of the event (as string).
 * - `paid` – Updated paid status.
 * - `participantLimit` – Updated participant limit. Must be zero or positive.
 * - `requestModeration` – Updated moderation requirement.
 * - `stateAction` – User-specific moderation state action.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventUserRequestDto {
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    @Size(min = 3, max = 120)
    String title;

    @Size(min = 20, max = 2000)
    String annotation;

    @Size(min = 20, max = 7000)
    String description;

    Long category;

    LocationDto location;
    String eventDate;

    Boolean paid;
    @PositiveOrZero
    Integer participantLimit;
    Boolean requestModeration;

    UpdateUserStateAction stateAction;
}
