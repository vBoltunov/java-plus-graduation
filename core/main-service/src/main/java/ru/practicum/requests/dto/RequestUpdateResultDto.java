package ru.practicum.requests.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * DTO representing the result of updating participation requests.
 *
 * Fields:
 * - `confirmedRequests` – List of requests successfully confirmed.
 * - `rejectedRequests` – List of requests that were rejected.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestUpdateResultDto {
    List<ParticipationRequestDto> confirmedRequests;
    List<ParticipationRequestDto> rejectedRequests;
}
