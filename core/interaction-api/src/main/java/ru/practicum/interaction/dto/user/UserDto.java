package ru.practicum.interaction.dto.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * DTO representing full user data.
 *
 * Fields:
 * - `id` – Unique identifier of the user.
 * - `name` – Full name of the user.
 * - `email` – Email address of the user.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    String name;
    String email;
}
