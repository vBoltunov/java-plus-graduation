package ru.practicum.compilation.dto;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for updating an existing compilation.
 *
 * Fields:
 * - `events` – Updated list of event IDs for the compilation. Defaults to empty.
 * - `pinned` – Updated pinned status of the compilation.
 * - `title` – Updated title of the compilation.
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDtoUpdate {
    @Builder.Default
    List<Long> events = new ArrayList<>();
    Boolean pinned;

    @Size(min = 1, max = 50, message = "Длина заголовка от 1 до 50 символов")
    String title;
}
