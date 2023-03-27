package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;

import javax.persistence.EntityNotFoundException;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    private final String myTextError = "%s";




    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorResponse handleBadRequestException(final BadRequestException e) {
        log.warn("Ошибка {}", e.getMessage(), e);
        return new ErrorResponse(String.format(myTextError, e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorResponse handleIMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.warn("Ошибка {}", e.getMessage(), e);
        return new ErrorResponse(
                String.format(myTextError, e.getMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.warn("Ошибка {}", e.getMessage(), e);
        return new ErrorResponse(String.format(myTextError, e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorResponse handleEntityNotFoundException(final EntityNotFoundException e) {
        log.warn("Ошибка {}", e.getMessage(), e);
        return new ErrorResponse(String.format(myTextError, e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500 — если возникло исключение.
    public ErrorResponse handleIAnyException(final Exception e) {
        log.warn("Ошибка {}", e.getMessage(), e);
        return new ErrorResponse(
                String.format(myTextError, e.getMessage())
        );
    }
}
