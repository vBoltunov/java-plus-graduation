package ru.practicum.events.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * Entity representing a geographic location.
 *
 * Fields:
 * - `id` – Unique identifier of the location.
 * - `lat` – Latitude coordinate of the location.
 * - `lon` – Longitude coordinate of the location.
 */
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "locations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Float lat;
    Float lon;
}
