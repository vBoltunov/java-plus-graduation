package ru.practicum.eventservice.events.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * Entity representing a geographic location.
 *
 * Fields:
 * - `lat` – Latitude coordinate of the location.
 * - `lon` – Longitude coordinate of the location.
 */
@Getter
@Setter
@ToString
@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {
    @Column(name = "latitude")
    Float lat;

    @Column(name = "longitude")
    Float lon;
}