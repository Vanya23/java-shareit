package ru.practicum.shareit.error.exception;

public class EmptyEmailException extends Exception {
    public EmptyEmailException() {
    }

    public EmptyEmailException(String message) {
        super(message);
    }
}
