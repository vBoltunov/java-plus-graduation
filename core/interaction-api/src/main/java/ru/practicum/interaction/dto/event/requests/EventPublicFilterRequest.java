package ru.practicum.interaction.dto.event.requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.interaction.dto.event.validators.StartAndEndValid;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Filter criteria for public event searches.
 *
 * Fields:
 * - `text` – Text to search in event title and description (case-insensitive). Optional.
 * - `categories` – List of category IDs to filter by. Empty list means no filtering.
 * - `paid` – Filter by paid/free events. True = paid only, false = free only, null = no filter.
 * - `rangeStart` – Earliest event date (format: "yyyy-MM-dd HH:mm:ss"). Must be before rangeEnd if both present.
 * - `rangeEnd` – Latest event date (format: "yyyy-MM-dd HH:mm:ss"). Must be after rangeStart if both present.
 * - `onlyAvailable` – If true, returns only events with available participation slots.
 * - `sort` – Sort option: "EVENT_DATE" (default) or "VIEWS". Case-sensitive.
 */
@Data
@StartAndEndValid
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventPublicFilterRequest {
    String text;

    List<Long> categories;

    Boolean paid;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime rangeStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime rangeEnd;

    boolean onlyAvailable;

    String sort;
}