package ru.practicum.events.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

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
public class EventShortDto {
    Long id;
    String annotation;
    boolean paid;

    String title;
    String eventDate;

    CategoryDto category;
    UserShortDto initiator;

    Long confirmedRequests;
    Long views;

}