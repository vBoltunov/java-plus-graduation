package ru.practicum.compilation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.events.model.Event;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compilations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "pinned", nullable = false)
    Boolean pinned;

    @Column(name = "title", nullable = false, length = 50)
    String title;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(
            name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id")
    )
    Set<Event> events;
}