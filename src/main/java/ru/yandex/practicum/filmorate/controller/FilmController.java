package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @PostMapping
    public Film addFilms(@Valid @RequestBody Film film) { //Добавление фильма
        validationFilm(film);
        log.debug("Фильм добавлен");
        film.setId(id);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) { //Обновление фильма
        validationFilm(film);
        if (films.containsKey(film.getId())) {
            log.debug("Фильм обновлен");
            films.put(film.getId(), film);
        } else {
            log.debug("Фильм не существует");
            throw new ValidationException("Данного фильма нет в базе данных");
        }
        return film;
    }

    @GetMapping
    public Collection<Film> getFilm() { //Получение списка фильмов
        log.debug("Запрошен список фильмов, их количество: " + films.size());
        return films.values();
    }

    public void validationFilm(Film film) {
        String str = film.getDescription();
        char[] strToArray = str.toCharArray(); // Преобразуем строку str в массив символов (char)
        if (strToArray.length > 200) {
            log.debug("Длина описание фильма > 200");
            throw new ValidationException("Описание содержит " + str.length() + " символов. " + "Максимальная длина - 200");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Дата релиза < 28.12.1895");
            throw new ValidationException("Ваша дата " + film.getReleaseDate() + " допустимая ранняя дата 28.12.1895");
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
}
