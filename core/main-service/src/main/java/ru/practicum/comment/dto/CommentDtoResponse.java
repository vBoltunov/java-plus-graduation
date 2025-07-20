package ru.practicum.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * DTO representing a submitted comment with metadata.
 *
 * Fields:
 * - `id` – Unique identifier of the comment.
 * - `authorId` – Identifier of the user who authored the comment.
 * - `eventId` – Identifier of the event associated with the comment.
 * - `created` – Timestamp of comment creation (formatted as `yyyy-MM-dd HH:mm:ss`).
 * - `message` – Text content of the comment.
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDtoResponse {
    Long id;
    Long authorId;
    Long eventId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;
    String message;
}
