package ru.practicum.interaction.dto.event.compilation;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.Set;

/**
 * DTO for creating a new compilation.
 *
 * Fields:
 * - `events` – List of event IDs to include in the compilation. Defaults to empty.
 * - `pinned` – Specifies if the compilation is pinned.
 * - `title` – Title of the compilation. Must not be blank.
 */
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDtoRequest {
    Set<Long> events;

    boolean pinned;

    @Size(min = 1, max = 50)
    String title;
}