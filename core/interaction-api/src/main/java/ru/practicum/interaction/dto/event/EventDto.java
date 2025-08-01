package ru.practicum.interaction.dto.event;

import java.time.LocalDateTime;

/**
 * DTO representing an event.
 *
 * Fields:
 * - `id` – Unique identifier of the event.
 * - `eventDate` – Date and time when the event occurs.
 * - `views` – Number of views/visits for the event (can be set manually).
 * - `confirmedRequests` – Number of confirmed participation requests (can be set manually).
 */
public interface EventDto {
    Long getId();

    LocalDateTime getEventDate();

    void setViews(Long views);

    void setConfirmedRequests(Long confirmedRequests);
}