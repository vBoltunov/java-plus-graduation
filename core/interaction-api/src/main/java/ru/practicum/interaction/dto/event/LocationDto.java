package ru.practicum.interaction.dto.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * DTO representing geographical coordinates.
 *
 * Fields:
 * - `lat` – Latitude coordinate (valid range: -90 to 90).
 * - `lon` – Longitude coordinate (valid range: -180 to 180).
 */
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationDto {
    Float lat;
    Float lon;
}