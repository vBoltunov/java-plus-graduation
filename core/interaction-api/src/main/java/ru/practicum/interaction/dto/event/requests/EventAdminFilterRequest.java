package ru.practicum.interaction.dto.event.requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.interaction.dto.event.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.interaction.util.ConstantsUtil.DATE_TIME_PATTERN;

/**
 * Filter criteria for admin event searches.
 *
 * Fields:
 * - `userIds` – List of user IDs to filter by. Optional.
 * - `states` – List of event states to filter by (e.g., PENDING, PUBLISHED). Optional.
 * - `categories` – List of category IDs to filter by. Optional.
 * - `rangeStart` – Start date/time for filtering events (format: "yyyy-MM-dd HH:mm:ss"). Optional.
 * - `rangeEnd` – End date/time for filtering events (format: "yyyy-MM-dd HH:mm:ss"). Optional.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventAdminFilterRequest {
    List<Long> userIds;

    List<EventState> states;

    List<Long> categories;

    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    LocalDateTime rangeStart;

    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    LocalDateTime rangeEnd;
}