package ru.practicum.shareit.error.exception;

public class IncorrectEmailException extends Exception {
    public IncorrectEmailException() {
    }

    public IncorrectEmailException(String message) {
        super(message);
    }
}
