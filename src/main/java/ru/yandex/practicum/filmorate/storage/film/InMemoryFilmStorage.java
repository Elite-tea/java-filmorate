package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.Validation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private Long id = 1L;

    /**
     * Добавление фильма.
     *
     * @param film информация о фильме.
     */

    @PostMapping
    public Film addFilms(@Valid @RequestBody Film film) {
        Validation.validationFilm(film);
        log.debug("Фильм добавлен");
        film.setId(id);
        films.put(film.getId(), film);
        id++;
        return film;
    }


    /**
     * Обновление фильма.
     *
     * @param film информация о фильме.
     * @throws NotFoundException генерирует 404 ошибку в случае если фильма не существует.
     */

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        Validation.validationFilm(film);
        if (films.containsKey(film.getId())) {
            log.debug("Фильм обновлен");
            films.put(film.getId(), film);
        } else {
            log.debug("Фильм не существует");
            throw new NotFoundException("Данного фильма нет в базе данных");
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

    /**
     * Получение фильма по id.
     *
     * @param id айди фильма.
     * @throws NotFoundException генерирует 404 ошибку в случае если фильма не существует.
     */

    @GetMapping()
    public Film getByIdFilm(Long id) {
        if (films.containsKey(id)) {
            log.debug("Запрошен список фильмов, их количество: {} ", films.size());
            return films.get(id);
        } else {
            log.debug("Фильм не существует");
            throw new NotFoundException(String.format("Фильм с id %s не существует", films.get(id)));
        }
    }
}