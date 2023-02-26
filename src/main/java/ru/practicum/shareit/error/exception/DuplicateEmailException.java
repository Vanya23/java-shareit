package ru.practicum.shareit.error.exception;

public class DuplicateEmailException extends Exception {
    public DuplicateEmailException() {
    }

    public DuplicateEmailException(String message) {
        super(message);
    }
}
