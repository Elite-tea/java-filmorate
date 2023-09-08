package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Класс-исключение для генерации ошибки 404.
 */
@Slf4j
public class NotFoundException extends RuntimeException {
    public NotFoundException(final String message) {
        super(message);
        log.error(message);
    }
}