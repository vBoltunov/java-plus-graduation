package ru.practicum.events.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for a new event creation.
 *
 * Fields:
 * - `annotation` – Short event description. Must not be blank.
 *                  Length: 20–2000 characters.
 * - `title` – Title of the event. Must not be blank.
 *            Length: 3–120 characters.
 * - `eventDate` – Scheduled date/time of the event (as string). Must not be blank.
 * - `description` – Full description. Must not be blank.
 *                  Length: 20–7000 characters.
 * - `category` – ID of the selected category. Must not be null.
 * - `location` – Location of the event. Must not be null.
 * - `paid` – Whether participation is paid.
 * - `participantLimit` – Maximum allowed participants. Must be zero or positive.
 * - `requestModeration` – Whether participation requests require approval. Defaults to true.
 */
@Data
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation;

    @NotBlank
    @Size(min = 3, max = 120)
    String title;

    @NotBlank
    String eventDate;

    @NotBlank
    @Size(min = 20, max = 7000)
    String description;

    @NotNull
    Long category;

    @NotNull
    LocationDto location;

    Boolean paid;

    @PositiveOrZero
    Integer participantLimit;

    Boolean requestModeration = true;

}