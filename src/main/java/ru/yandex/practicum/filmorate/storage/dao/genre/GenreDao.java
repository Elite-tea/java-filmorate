package ru.yandex.practicum.filmorate.storage.dao.genre;

import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.Set;

/**
 * Интерфейс для работы с логикой касающиеся жанров, реализован в {@link GenreDaoImpl}
 */
public interface GenreDao {
    /**
     * Метод получения жанра по его идентификатору
     *
     * @param id идентификатор жанра
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id жанра
     */
    Genre getGenreById(Integer id);

    /**
     * Метод получает коллекцию жанров в LinkedHashSet.
     *
     * @return возвращает коллекцию жанров
     */
    Set<Genre> getGenres();

    /**
     * Метод добавления коллекции жанров фильму
     *
     * @param filmId идентификатор фильма
     * @param genres коллекция жанров
     */
    void addGenres(Long filmId, HashSet<Genre> genres);

    /**
     * Метод обновления коллекции жанров у фильма
     *
     * @param filmId идентификатор фильма
     * @param genres коллекция жанров
     */
    void updateGenres(Long filmId, HashSet<Genre> genres);

}
