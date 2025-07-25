package ru.practicum.category.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Data Transfer Object (DTO) representing a category.
 *
 * Fields:
 * - `id` – Unique identifier of the category.
 * - `name` – Name of the category.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto {
    Long id;
    String name;
}