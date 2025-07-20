package ru.practicum.exception;

public class StatsServerUnavailable extends RuntimeException {
    public StatsServerUnavailable(String message) {
        super(message);
    }

    public StatsServerUnavailable(String message, Throwable cause) {
        super(message, cause);
    }
}
