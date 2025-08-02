package ru.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.ConstantsUtil.DATE_TIME_PATTERN;

/**
 * DTO for passing search/filter criteria for statistics queries.
 *
 * Fields:
 * - `start` – Start of the time period (format: yyyy-MM-dd HH:mm:ss). Required.
 * - `end` – End of the time period (format: yyyy-MM-dd HH:mm:ss). Required.
 * - `uris` – List of URIs to filter by. Optional.
 * - `unique` – If true, counts only unique IPs. Optional.
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsCriteriaDto {
    @NotNull
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    LocalDateTime start;
    @NotNull
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    LocalDateTime end;
    List<String> uris;
    Boolean unique;
}
