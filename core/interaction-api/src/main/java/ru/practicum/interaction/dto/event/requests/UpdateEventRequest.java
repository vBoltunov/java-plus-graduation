package ru.practicum.interaction.dto.event.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.interaction.dto.event.LocationDto;

import java.time.LocalDateTime;

/**
 * Base request DTO for updating event properties.
 *
 * Fields:
 * - `annotation` – Brief event summary (20-2000 characters).
 * - `category` – ID of the event category. Null preserves current value.
 * - `description` – Full event description (20-7000 characters).
 * - `eventDate` – New date/time for event (format: "yyyy-MM-dd HH:mm:ss").
 * - `location` – Venue coordinates (latitude/longitude). Null preserves current location.
 * - `paid` – Whether event requires payment: true/false. Null preserves current setting.
 * - `participantLimit` – Maximum attendees (positive integer). 0 = unlimited.
 * - `requestModeration` – Whether requests require moderation: true/false. Null preserves current setting.
 * - `title` – Event name (3-120 characters).
 */
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventRequest {
    @Size(min = 20, max = 2000)
    String annotation;

    Long category;

    @Size(min = 20, max = 7000)
    String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    LocationDto location;

    Boolean paid;

    @Positive
    Integer participantLimit;

    Boolean requestModeration;

    @Size(min = 3, max = 120)
    String title;
}