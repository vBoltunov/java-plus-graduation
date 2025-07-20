package ru.practicum.events.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO representing a location.
 *
 * Fields:
 * - `lat` – Latitude. Must not be null.
 * - `lon` – Longitude. Must not be null.
 */
@Data
public class LocationDto {
    @NotNull
    Float lat;
    @NotNull
    Float lon;
}