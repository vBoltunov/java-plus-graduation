package ru.practicum.events.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.events.model.EventState;
import ru.practicum.user.dto.UserShortDto;

/**
 * DTO representing full details of an event.
 *
 * Fields:
 * - `id` – Unique identifier of the event.
 * - `annotation` – Short description of the event.
 * - `paid` – Indicates if the event is paid.
 * - `title` – Title of the event.
 * - `eventDate` – Scheduled time (as string) of the event.
 * - `description` – Detailed description of the event.
 * - `requestModeration` – Whether moderation is required.
 * - `participantLimit` – Maximum participants allowed.
 * - `publishedOn` – Publication timestamp (as string).
 * - `createdOn` – Creation timestamp (as string).
 * - `category` – Category DTO of the event.
 * - `initiator` – Initiator DTO of the event.
 * - `location` – Location DTO of the event.
 * - `state` – Moderation state of the event.
 * - `confirmedRequests` – Confirmed participation count.
 * - `views` – Number of event views.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
    Long id;
    String annotation;
    boolean paid;
    String title;
    String eventDate;
    String description;
    boolean requestModeration;
    int participantLimit;
    String publishedOn;
    String createdOn;
    CategoryDto category;
    UserShortDto initiator;
    LocationDto location;
    EventState state;
    Integer confirmedRequests;
    Long views;
}