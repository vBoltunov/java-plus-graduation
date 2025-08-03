package ru.practicum.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * DTO representing detailed information about a recorded statistic.
 *
 * Fields:
 * - `app` – Name of the application recording the request. Must not be empty.
 * - `uri` – Requested URI path. Must not be empty.
 * - `ip` – IP address from which the request originated.
 * - `timestamp` – Date and time of the recorded request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HitDto {
    @NotEmpty
    String app;
    @NotEmpty
    String uri;
    String ip;
    String timestamp;
}
