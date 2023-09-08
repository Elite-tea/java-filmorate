package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Класс-исключение для генерации ошибки 400.
 */
@Slf4j
public class ValidationException extends RuntimeException {

    public ValidationException(final String message) {
        super(message);
        log.error(message);
    }
}