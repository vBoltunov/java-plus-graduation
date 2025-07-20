package ru.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing detailed information about a recorded statistic.
 *
 * Fields:
 * - `id` – Unique identifier of the statistic entry.
 * - `app` – Name of the application recording the request. Must not be blank.
 * - `uri` – Requested URI path. Must not be blank.
 * - `ip` – IP address from which the request originated. Must not be blank.
 * - `timestamp` – Date and time of the recorded request. Must not be null.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatDto {
    Long id;
    @NotBlank
    String app;
    @NotBlank
    String uri;
    @NotBlank
    String ip;
    @NotNull
    String timestamp;
}