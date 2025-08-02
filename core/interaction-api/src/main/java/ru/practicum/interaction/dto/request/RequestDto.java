package ru.practicum.interaction.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static ru.practicum.interaction.util.ConstantsUtil.DATE_TIME_PATTERN;

/**
 * DTO representing a participation request.
 *
 * Fields:
 * - `id` – Unique identifier of the request.
 * - `created` – Timestamp of request creation.
 * - `event` – Identifier of the target event.
 * - `requester` – Identifier of the user who submitted the request.
 * - `status` – Current status of the request.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDto {
    Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    LocalDateTime created;
    Long event;
    Long requester;
    String status;
}
