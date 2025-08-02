package ru.practicum.interaction.dto.event.compilation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.interaction.dto.event.EventShortDto;

import java.util.Set;

/**
 * DTO representing a compilation with metadata and event details.
 *
 * Fields:
 * - `id` – Unique identifier of the compilation.
 * - `events` – List of event summaries included in the compilation.
 * - `pinned` – Indicates whether the compilation is pinned.
 * - `title` – Title of the compilation.
 */
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDtoResponse {
    Long id;
    Set<EventShortDto> events;
    boolean pinned;
    String title;
}
