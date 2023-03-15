package ru.practicum.shareit.error.exception;

public class IncorrectIdUser extends Exception {
    public IncorrectIdUser() {
    }

    public IncorrectIdUser(String message) {
        super(message);
    }
}
