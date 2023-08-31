package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Класс-исключение для генерации ошибки 500.
 */
@Slf4j
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(final String message) {
        super(message);
        log.error(message);
    }
}
