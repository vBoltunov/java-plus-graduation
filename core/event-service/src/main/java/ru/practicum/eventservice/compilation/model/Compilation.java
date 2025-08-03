package ru.practicum.eventservice.compilation.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.practicum.eventservice.events.model.Event;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a compilation of events.
 *
 * Fields:
 * - `id` – Unique identifier of the compilation. Auto-generated.
 * - `pinned` – Indicates whether the compilation is pinned for highlighting.
 * - `title` – Title of the compilation. Limited to 50 characters.
 * - `events` – Set of events included in the compilation. Cascade delete enabled via join table `compilations_events`.
 */
@Getter
@Setter
@Entity
@ToString
@Table(name = "compilations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "pinned", nullable = false)
    Boolean pinned;

    @Column(name = "title", nullable = false)
    String title;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    Set<Event> events = new HashSet<>();
}