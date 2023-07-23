package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class Validation {

    private Validation() {

    }

    /**
     * Проверка фильма на корректность.
     */

    public static void validationFilm(Film film) {
        String str = film.getDescription();
        char[] strToArray = str.toCharArray(); // Преобразуем строку str в массив символов (char)
        if (strToArray.length > 200) {
            log.debug("Длина описание фильма > 200");
            throw new ValidationException(String.format("Описание содержит %s символов. " + "Максимальная длина - 200", str.length()));
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Дата релиза < 28.12.1895");
            throw new ValidationException((String.format("Ваша дата %s допустимая ранняя дата 28.12.1895", film.getReleaseDate())));
        }

        if (film.getName() == null || film.getName().isBlank() || film.getName().isEmpty()) {
            log.debug("Название фильма пустое");
            throw new ValidationException("Фильм не имеет названия.");
        }

        if (film.getDuration() < 0) {
            log.debug("Длительность меньше 0");
            throw new ValidationException("Отрицательная длительность фильма");
        }
    }

    /**
     * Проверка пользователя на корректность.
     */


    public static void validationUser(User user) {
        char[] nameChar = user.getLogin().toCharArray();

        for (char c : nameChar) {
            if (c == ' ') { // Для наглядности вставим пробел между индексами
                throw new ValidationException("Логин содержит пробел");
            }
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("У пользователя нет имени, используем логин");
            user.setName(user.getLogin());
        }

        if ((user.getLogin() == null || user.getLogin().isBlank())) {
            log.debug("У пользователя нет логина");
            user.setName(user.getLogin());
            throw new ValidationException("Отсутствует или не верный логин");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Не верная ДР");
            throw new ValidationException(String.format("Вы путешествуете во времени? Дата рождения не может быть позже %s", LocalDate.now()));
        }

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException(String.format("Не верный email у пользователя %s", user.getId()));
        }
    }

}
