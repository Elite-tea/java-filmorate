package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.genre.GenreDao;

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
            throw new NotFoundException("Передан пустой идентификатор жанра");
        }
        try {
            return genreDao.getGenreById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException(String.format("Жанр с id %d не существует", id));
        }
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
