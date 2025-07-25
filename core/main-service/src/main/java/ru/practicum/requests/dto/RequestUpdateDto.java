package ru.practicum.requests.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * DTO for updating the status of multiple participation requests.
 *
 * Fields:
 * - `requestIds` – List of request IDs to be updated.
 * - `status` – New status to apply to the requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestUpdateDto {
    List<Long> requestIds;
    String status;
}
