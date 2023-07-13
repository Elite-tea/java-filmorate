package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.Validation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    Validation validation = new Validation();
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    /**
     * Добавление фильма.
     *
     * @param film информация о фильме.
     */

    @PostMapping
    public Film addFilms(@Valid @RequestBody Film film) {
        validation.validationFilm(film);
        log.debug("Фильм добавлен");
        film.setId(id);
        films.put(film.getId(), film);
        return film;
    }


    /**
     * Обновление фильма.
     *
     * @param film информация о фильме.
     */

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        validation.validationFilm(film);
        if (films.containsKey(film.getId())) {
            log.debug("Фильм обновлен");
            films.put(film.getId(), film);
        } else {
            log.debug("Фильм не существует");
            throw new ValidationException("Данного фильма нет в базе данных");
        }
        return film;
    }

    /**
     * Получение списка фильмов.
     *
     * @return films возвращает коллекцию фильмов.
     */

    @GetMapping
    public Collection<Film> getFilm() {
        log.debug("Запрошен список фильмов, их количество: {} ", films.size());
        return films.values();
    }
}
