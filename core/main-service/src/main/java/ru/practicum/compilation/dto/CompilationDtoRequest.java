package ru.practicum.compilation.dto;

import jakarta.validation.constraints.NotBlank;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDtoRequest {
    @Builder.Default
    List<Long> events = new ArrayList<>();
    Boolean pinned;

    @NotBlank(message = "Заголовок должен быть заполнен")
    @Size(min = 1, max = 50, message = "Длина заголовка от 1 до 50 символов")
    String title;
}