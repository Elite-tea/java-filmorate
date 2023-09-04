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
    Film addFilms(Film film);

    /**
     * Метод изменения фильма
     *
     * @param film объект фильма
     * @return возвращает обновленный фильм
     */
    Film put(Film film);

    /**
     * Запрос коллекции фильмов
     *
     * @return возвращает коллекцию фильмов
     */
    Collection<Film> getFilm();

    /**
     * Запрос фильма по id
     *
     * @param id идентификатор фильма
     * @return возвращает фильм с указанным id
     */
    Film getByIdFilm(Long id);

    /**
     * Метод получения жанра по идентификатору фильма
     *
     * @param filmId идентификатор фильма
     * @return возвращает коллекцию жанров фильма
     */
    HashSet<Genre> getGenresByFilm(Long filmId);

    /**
     * поиск по названию фильмов и по режиссёру
     *
     * @param query — текст для поиска,
     * @param by    — может принимать значения director (поиск по режиссёру), title (поиск по названию),
     *              либо оба значения через запятую при поиске одновременно и по режиссеру и по названию.
     * @return возвращает список фильмов с количеством лайков (От большего к меньшему)
     */
    List<Film> getSearchResult(String query, String by);
}
