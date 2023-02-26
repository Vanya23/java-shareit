package ru.practicum.shareit.error.exception;

public class IncorrectItemException extends Exception {
    public IncorrectItemException() {
    }

    public IncorrectItemException(String message) {
        super(message);
    }
}
