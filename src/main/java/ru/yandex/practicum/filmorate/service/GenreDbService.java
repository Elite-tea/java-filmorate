package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.Collection;

/**
 * Класс-сервис с логикой для оперирования жанрами
 */
@Service
@RequiredArgsConstructor
public class GenreDbService {
    /**
     * Поле для доступа к операциям с жанрами
     */
    private final GenreDao genreDao;

    /**
     * Метод получает жанр по его идентификатору.
     *
     * @param id идентификатор запрашиваемого жанра
     * @return возвращает объект жанра с указанным id
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id жанра.
     */
    public Genre getGenreById(Integer id) {
        if (id == null) {
            throw new NotFoundException(String.format("Жанра с id %s не существует", id));
        }
        return genreDao.getGenreById(id);
    }

    /**
     * Метод получает коллекцию жанров.
     *
     * @return возвращает коллекцию жанров
     */
    public Collection<Genre> getGenres() {
        return genreDao.getGenres();
    }

}
