package ru.practicum.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO for submitting a new user comment.
 *
 * Fields:
 * - `message` – Text of the comment. Must not be blank.
 *              Length constraints: 2–1000 characters.
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoRequest {
    @NotBlank(message = "Сообщение не может быть пустым.")
    @Size(min = 2, max = 1000, message = "Размер сообщения от 2 до 1000 символов.")
    private String message;
}
