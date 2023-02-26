package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.error.exception.*;


@RestControllerAdvice
public class UserErrorHandler {
    private final String MYTEXTERROR = "Ошибка %s";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleDuplicateEmailException(final DuplicateEmailException e) {
        return new ErrorResponse(String.format(MYTEXTERROR, e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEmptyEmailException(final EmptyEmailException e) {
        return new ErrorResponse(String.format(MYTEXTERROR, e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectEmailException(final IncorrectEmailException e) {
        return new ErrorResponse(String.format(MYTEXTERROR, e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectItemException(final IncorrectItemException e) {
        return new ErrorResponse(String.format(MYTEXTERROR, e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleIncorrectIdUser(final IncorrectIdUser e) {
        return new ErrorResponse(String.format(MYTEXTERROR, e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleIncorrectIdUserInClassItem(final IncorrectIdUserInClassItem e) {
        return new ErrorResponse(String.format(MYTEXTERROR, e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleOtherOwnerItemException(final OtherOwnerItemException e) {
        return new ErrorResponse(String.format(MYTEXTERROR, e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500 — если возникло исключение.
    public ErrorResponse handleIAnyException(final Throwable e) {
        return new ErrorResponse(
                String.format(MYTEXTERROR, e.getMessage())
        );
    }
}
