package ru.practicum.exception;

public class StatsClientException extends RuntimeException {
    public StatsClientException(int statusCode, String message) {
        super("StatusCode is " + statusCode + ".\n" + message);
    }
}