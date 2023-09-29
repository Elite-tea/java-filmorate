package ru.yandex.practicum.filmorate.storage.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.assistant.SortBy;
import ru.yandex.practicum.filmorate.validation.Validation;

import javax.validation.Valid;
import java.util.*;

/**
 * Класс-хранилище реализующий интерфейс {@link FilmStorage} для хранения и обновления фильмов со свойствами <b>films<b/> и <b>id<b/>
 */
@Slf4j
@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    /**
     * Поле хранилище фильмов
     */
    private final Map<Long, Film> films = new HashMap<>();
    /**
     * Поле счетчик идентификаторов фильмов
     */
    private Long id = 1L;

    /**
     * Метод добавление фильма.
     *
     * @param film информация о фильме.
     * @return возвращает созданный фильм
     */
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        Validation.validationFilm(film);
        log.debug("Фильм добавлен");
        film.setId(id);
        films.put(film.getId(), film);
        id++;
        return film;
    }

    /**
     * Метод обновления фильма.
     *
     * @param film информация о фильме.
     * @return возвращает обновленный фильм
     * @throws NotFoundException генерирует 404 ошибку в случае если фильма не существует.
     */
    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        Long filmId = film.getId();

        Validation.validationFilm(film);
        if (films.containsKey(filmId)) {
            log.debug("Фильм обновлен");
            films.put(filmId, film);
        } else {
            log.debug(String.format("Фильм с id %s не существует", filmId));
            throw new NotFoundException("Данного фильма нет в базе данных");
        }
        return film;
    }

    /**
     * Заглушка
     */
    @Override
    public void deleteFilm(Long id) {
    }

    /**
     * Метод получения списка фильмов.
     *
     * @return films возвращает коллекцию фильмов.
     */
    @GetMapping
    public Collection<Film> getFilm() {
        log.debug("Запрошен список фильмов, их количество: {} ", films.size());
        return films.values();
    }

    /**
     * Метод получения фильма по id.
     *
     * @param id айди фильма.
     * @return возвращает фильм с указанным id
     * @throws NotFoundException генерирует 404 ошибку в случае если фильма не существует.
     */
    @GetMapping()
    public Film getFilmById(Long id) {
        if (films.containsKey(id)) {
            log.debug("Запрошен фильм с id : {} ", id);
            return films.get(id);
        } else {
            log.debug("Фильм не существует");
            throw new NotFoundException(String.format("Фильм с id %s не существует", id));
        }
    }

    /**
     * Заглушка
     */
    @Override
    public Collection<Film> getFilms() {
        return Collections.emptyList();
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Collection<Film> getFilmsByUser(Long id) {
        return Collections.emptyList();
    }

    @Override
    public HashSet<Genre> getGenresByFilm(Long filmId) {
        return null;
    }

    @Override
    public List<Film> getDirectorFilms(Integer directorId, SortBy sortBy) {
        return null;
    }

    @Override
    public List<Film> getPopularFilmsByGenre(int count, int genreId) {
        return Collections.emptyList();
    }

    @Override
    public List<Film> getPopularFilmsByYear(int count, int year) {
        return Collections.emptyList();
    }

    @Override
    public List<Film> getPopularFilmsByGenreAndYear(int count, int genreId, int year) {
        return Collections.emptyList();
    }

    @Override
    public List<Film> getSearchResult(String query, String by) {
        return null;
    }
}