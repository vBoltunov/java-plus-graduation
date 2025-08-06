package ru.practicum.interaction.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.interaction.dto.event.category.CategoryDto;
import ru.practicum.interaction.dto.user.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.interaction.util.ConstantsUtil.DATE_TIME_PATTERN;


/**
 * DTO representing brief information about an event.
 *
 * Fields:
 * - `id` – Unique identifier of the event.
 * - `annotation` – Short description of the event.
 * - `paid` – Whether participation is paid.
 * - `title` – Title of the event.
 * - `eventDate` – Scheduled time (as string) of the event.
 * - `category` – Category DTO.
 * - `initiator` – Initiator DTO.
 * - `confirmedRequests` – Count of confirmed participations.
 * - `views` – Number of views of the event.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto implements EventDto {
    Long id;
    String annotation;
    boolean paid;
    String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    LocalDateTime eventDate;
    CategoryDto category;
    UserShortDto initiator;
    Long confirmedRequests;
    double rating;
}