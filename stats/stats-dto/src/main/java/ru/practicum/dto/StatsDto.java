package ru.practicum.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * DTO representing aggregated view statistics.
 *
 * Fields:
 * - `app` – Name of the application.
 * - `uri` – Specific URI being tracked.
 * - `hits` – Total number of requests recorded for the given URI.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsDto {
    String app;
    String uri;
    Long hits;
}
