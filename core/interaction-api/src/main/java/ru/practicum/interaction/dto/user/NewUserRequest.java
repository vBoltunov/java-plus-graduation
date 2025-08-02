package ru.practicum.interaction.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * DTO representing a request to create a new user.
 *
 * Fields:
 * - `name` – User's name. Must not be blank.
 *            Validation: length between 2 and 250 characters.
 * - `email` – Email address. Must be valid and not blank.
 *            Validation: length between 6 and 254 characters.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewUserRequest {
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 250, message = "Имя должно быть от 2 до 250 символов")
    String name;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен быть валидным")
    @Size(min = 6, max = 254, message = "Email должен быть от 6 до 254 символов")
    String email;
}