package ru.yandex.practicum.filmorate.Exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String s) {
        super(s);
    }
}
