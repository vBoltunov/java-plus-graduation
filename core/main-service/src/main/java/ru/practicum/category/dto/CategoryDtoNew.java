package ru.practicum.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO used for creating a new category.
 *
 * Fields:
 * - `name` – Name of the category. Must not be blank.
 *            Length constraints: minimum 1 character, maximum 50 characters.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDtoNew {
    @NotBlank(message = "Название категории обязательно.")
    @Size(min = 1, max = 50,
            message = "Ограничение длины названия категории. Не более 50 символов и не менее 1 символа.")
    private String name;
}