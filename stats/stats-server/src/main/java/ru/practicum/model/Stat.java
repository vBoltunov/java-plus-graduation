package ru.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * Entity representing a record of request statistics.
 *
 * Fields:
 * - `id` – Unique identifier of the statistics entry. Auto-generated.
 * - `app` – Name of the application that recorded the statistic.
 * - `uri` – URI path that was accessed.
 * - `ip` – IP address from which the request originated.
 * - `timestamp` – Date and time when the statistic was recorded.
 */
@Entity
@Table(name = "stats")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String app;
    String uri;
    String ip;

    @Column(name = "date_stat")
    LocalDateTime timestamp;
}
