package ru.yandex.practicum.filmorate.storage.dao.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Интерфейс для работы с хранилищем фильмов, реализован в {@link InMemoryFilmStorage}
 */
public interface FilmStorage {
    /**
     * Метод добавления фильма
     *
     * @param film объект фильма
     * @return возвращает созданный фильм
     */
    Film addFilm(Film film);

    /**
     * Метод изменения фильма
     *
     * @param film объект фильма
     * @return возвращает обновленный фильм
     */
    Film updateFilm(Film film);

    /**
     * Запрос коллекции фильмов
     *
     * @return возвращает коллекцию фильмов
     */
    Collection<Film> getFilms();

    /**
     * Запрос фильма по id
     *
     * @param id идентификатор фильма
     * @return возвращает фильм с указанным id
     */
    Film getFilmById(Long id);

    /**
     * Метод получения жанра по идентификатору фильма
     *
     * @param filmId идентификатор фильма
     * @return возвращает коллекцию жанров фильма
     */
    HashSet<Genre> getGenresByFilm(Long filmId);

    /**
     * Метод получения фильмов
     * @param id
     * @return
     */
    Collection<Film> getFilmsByUser(Long id);

    /**
     * Метод получения списка популярных фильмов по идентификатору жанра
     *
     * @param count ограничение на колличество фильмов возвращаемых методом
     * @param genreId идентификатор жанра
     * @return возвращает коллекцию объектов типа фильм
     */
    List<Film> getPopularFilmsByGenre(int count, int genreId);

    /**
     * Метод получения списка популярных фильмов по году релиза фильма
     *
     * @param count ограничение на колличество фильмов возвращаемых методом
     * @param year год
     * @return возвращает коллекцию объектов типа фильм
     */
    List<Film> getPopularFilmsByYear(int count, int year);

    /**
     * Метод получения списка популярных фильмов по идентификатору жанра и году релиза
     *
     * @param count ограничение на колличество фильмов возвращаемых методом
     * @param genreId идентификатор жанра
     * @param year год
     * @return возвращает коллекцию объектов типа фильм
     */
    List<Film> getPopularFilmsByGenreAndYear(int count, int genreId, int year);
}
