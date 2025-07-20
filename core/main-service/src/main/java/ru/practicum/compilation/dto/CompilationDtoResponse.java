package ru.practicum.compilation.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.events.dto.EventShortDto;

import java.util.List;

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
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDtoResponse {
    Long id;
    List<EventShortDto> events;
    Boolean pinned;
    String title;
}
