package ru.practicum.requests.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO representing a participation request.
 *
 * Fields:
 * - `id` – Unique identifier of the request.
 * - `created` – Timestamp of request creation (formatted as string).
 * - `event` – Identifier of the target event.
 * - `requester` – Identifier of the user who submitted the request.
 * - `status` – Current status of the request.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequestDto {
    Long id;
    String created;
    Long event;
    Long requester;
    String status;
}
