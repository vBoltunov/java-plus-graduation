package ru.practicum.exception;

import org.springframework.http.HttpStatus;

public record ApiError(HttpStatus status, String error, String description, String stackTrace) {
}
