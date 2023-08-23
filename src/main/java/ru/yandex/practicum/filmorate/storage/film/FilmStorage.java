package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.HashSet;

/**
 * Интерфейс для работы с хранилищем фильмов, реализован в {@link InMemoryFilmStorage}
 */
public interface FilmStorage {
    /**
     * Метод добавления фильма, реализован в {@link InMemoryFilmStorage#addFilms(Film)}
     *
     * @param film объект фильма
     * @return возвращает созданный фильм
     */
    Film addFilms(Film film);

    /**
     * Метод изменения фильма, реализован в {@link InMemoryFilmStorage#put(Film)}
     *
     * @param film объект фильма
     * @return возвращает обновленный фильм
     */
    Film put(Film film);

    /**
     * Запрос коллекции фильмов, реализован в {@link InMemoryFilmStorage#getFilm()}
     */
    Collection<Film> getFilm();

    /**
     * Запрос фильма по id, реализован в {@link InMemoryFilmStorage#getByIdFilm(Long)}
     *
     * @param id идентификатор фильма
     * @return возвращает фильм с указанным id
     */
    Film getByIdFilm(Long id);

    HashSet<Genre> getGenresByFilm(Long filmId);
}
