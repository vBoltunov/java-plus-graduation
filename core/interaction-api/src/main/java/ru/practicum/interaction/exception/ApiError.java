package ru.practicum.interaction.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static ru.practicum.interaction.util.ConstantsUtil.DATE_TIME_PATTERN;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiError {
    String errors;
    String message;
    String reason;
    String status;
    @Builder.Default
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    LocalDateTime timestamp = LocalDateTime.now();
}
