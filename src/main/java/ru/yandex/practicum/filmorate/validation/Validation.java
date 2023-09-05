package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

/**
 * Утилитарный класс реализующий проверку соответствия данных в полях объектов с типом Film и User
 */
@Slf4j
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class Validation {
    /**
     * Пустой приватный конструктор для запрета создания экземпляров утилитарного класса
     */
    private Validation() {
    }

    /**
     * Проверка фильма на корректность.
     *
     * @param film объект для проверки.
     */
    public static void validationFilm(Film film) {
        log.debug("validationFilm({})", film);
        String str = film.getDescription();
        char[] strToArray = str.toCharArray(); // Преобразуем строку str в массив символов (char)
        if (strToArray.length > 200) {
            log.debug("Длина описание фильма > 200");
            throw new ValidationException(String.format("Описание содержит %s символов. Максимальная длина - 200",
                    str.length()));
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Дата релиза < 28.12.1895");
            throw new ValidationException((String.format("Ваша дата %s допустимая ранняя дата 28.12.1895",
                    film.getReleaseDate())));
        }

        if (film.getName() == null || film.getName().isBlank() || film.getName().isEmpty()) {
            log.debug("Название фильма пустое");
            throw new ValidationException("Фильм не имеет названия.");
        }

        if (film.getDuration() < 0) {
            log.debug("Длительность меньше 0");
            throw new ValidationException("Отрицательная длительность фильма");
        }
        log.trace("Фильм {} прошёл валидацию", film);
    }

    /**
     * Проверка пользователя на корректность.
     *
     * @param user объект для проверки.
     */
    public static void validationUser(User user) {
        log.debug("validationUser({})", user);
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
            throw new ValidationException(String.format(
                    "Вы путешествуете во времени? Дата рождения не может быть позже %s", LocalDate.now()));
        }

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException(String.format("Не верный email у пользователя %d", user.getId()));
        }
        log.trace("Пользователь {} прошёл валидацию", user);
    }

    /**
     * Проверка отзыва на корректность.
     *
     * @param review объект для проверки.
     */
    public static void validationReview(Review review) {
        log.debug("validationReview({})", review);
        if (review.getContent() == null || review.getContent().isBlank()) {
            throw new ValidationException("Поле с описанием отзыва не может быть пустым");
        }
        if (review.getIsPositive() == null) {
            throw new ValidationException("Попытка присвоить значению поля isPositive null");
        }
        review.setUseful(0);
        log.trace("Отзыв {} прошел валидацию", review);
    }

    /**
     * Проверка сущности режиссера на корректность.
     *
     * @param director объект для проверки.
     */
    public static void validationDirector(Director director) {
        log.debug("validationDirector({})", director);
        if (director.getName() == null || director.getName().isEmpty() || director.getName().isBlank()) {
            throw new ValidationException("Попытка присвоить пустое значение имени режиссера");
        }
        log.trace("Режиссер {} прошел валидацию", director);
    }

}
