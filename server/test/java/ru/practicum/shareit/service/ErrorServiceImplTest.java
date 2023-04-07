package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.ErrorHandler;
import ru.practicum.shareit.error.ErrorResponse;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;

import javax.persistence.EntityNotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

//@Rollback(false)
@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ErrorServiceImplTest {
    // реализация тестов

    private final ErrorHandler errorHandler;

    @Test
    void handleBadRequestException() {
        ErrorResponse out = errorHandler.handleBadRequestException(
                new BadRequestException("test"));
        assertThat(out, notNullValue());
    }

    @Test
    void handleNotFoundException() {
        ErrorResponse out = errorHandler.handleNotFoundException(
                new NotFoundException("test"));
        assertThat(out, notNullValue());
    }

    @Test
    void handleEntityNotFoundException() {
        ErrorResponse out = errorHandler.handleEntityNotFoundException(
                new EntityNotFoundException("test"));
        assertThat(out, notNullValue());
    }

    @Test
    void handleIAnyException() {
        ErrorResponse out = errorHandler.handleIAnyException(
                new Exception("test"));
        assertThat(out, notNullValue());
    }

}
