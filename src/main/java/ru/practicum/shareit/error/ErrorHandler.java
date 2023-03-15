package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.InternalServerErrortException;
import ru.practicum.shareit.error.exception.NotFoundException;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    private final String myTextError = "%s";
//    private final String myTextError = "Ошибка %s";

    //    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse handleDuplicateEmailException(final DuplicateEmailException e) {
//        log.warn("Ошибка {}", e.getMessage(), e);
//        return new ErrorResponse(String.format(myTextError, e.getMessage()));
//    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerErrorException(final InternalServerErrortException e) {
        log.warn("Ошибка {}", e.getMessage(), e);
        return new ErrorResponse(String.format(myTextError, e.getMessage()));
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handleEmptyEmailException(final EmptyEmailException e) {
//        log.warn("Ошибка {}", e.getMessage(), e);
//        return new ErrorResponse(String.format(myTextError, e.getMessage()));
//    }

    //    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handleIncorrectEmailException(final IncorrectEmailException e) {
//        log.warn("Ошибка {}", e.getMessage(), e);
//        return new ErrorResponse(String.format(myTextError, e.getMessage()));
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handleIncorrectItemException(final IncorrectItemException e) {
//        log.warn("Ошибка {}", e.getMessage(), e);
//        return new ErrorResponse(String.format(myTextError, e.getMessage()));
//    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorResponse handleBadRequestException(final BadRequestException e) {
        log.warn("Ошибка {}", e.getMessage(), e);
        return new ErrorResponse(String.format(myTextError, e.getMessage()));
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponse handleIncorrectIdUser(final IncorrectIdUser e) {
//        log.warn("Ошибка {}", e.getMessage(), e);
//        return new ErrorResponse(String.format(myTextError, e.getMessage()));
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponse handleIncorrectIdUserInClassItem(final IncorrectIdUserInClassItem e) {
//        log.warn("Ошибка {}", e.getMessage(), e);
//        return new ErrorResponse(String.format(myTextError, e.getMessage()));
//    }

    //    @ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponse handleOtherOwnerItemException(final OtherOwnerItemException e) {
//        log.warn("Ошибка {}", e.getMessage(), e);
//        return new ErrorResponse(String.format(myTextError, e.getMessage()));
//    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
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
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500 — если возникло исключение.
    public ErrorResponse handleIAnyException(final Exception e) {
        log.warn("Ошибка {}", e.getMessage(), e);
        return new ErrorResponse(
                String.format(myTextError, e.getMessage())
        );
    }
}
