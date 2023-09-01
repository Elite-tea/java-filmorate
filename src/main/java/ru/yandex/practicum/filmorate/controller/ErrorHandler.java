package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.InternalServiceException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

/**
 * Класс-контроллер для отлова ошибок, которые могут возникать на сервере
 */
@RestControllerAdvice
public class ErrorHandler {

    /**
     * Метод для отлова ошибки 404
     *
     * @param exception NotFoundException.class реализует ошибку
     * @return объект класса ошибки
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    /**
     * Метод для отлова ошибки 400
     *
     * @param exception ValidationException.class реализует ошибку
     * @return объект класса ошибки
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    /**
     * Метод для отлова ошибки 500
     *
     * @param exception InternalServiceError.class реализует ошибку
     * @return объект класса ошибки
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServiceException(final InternalServiceException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    /**
     * Метод для отлова ошибки 500
     *
     * @param exception AlreadyExistsException.class реализует ошибку
     * @return объект класса ошибки
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAlreadyExistsException(final AlreadyExistsException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
