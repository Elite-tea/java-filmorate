package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.dao.LikeDao;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс-сервис с логикой для оперирования фильмами с хранилищами <b>filmDbStorage<b/> и <b>userDbStorage<b/>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FilmDbService {
    /**
     * Поле с прошлой версией хранилища фильмов
     */
    private final FilmStorage filmStorage;
    /**
     * Поле с прошлой версией хранилища пользователей
     */
    private final UserStorage userStorage;
    /**
     * Поле для доступа к операциям с жанрами
     */
    private final GenreDao genreDao;
    /**
     * Поле для доступа к операциям с рейтингом
     */
    private final MpaDao mpaDao;
    /**
     * Поле для доступа к операциям с лайками
     */
    private final LikeDao likeDao;

    /**
     * Конструктор сервиса.
     *
     * @see FilmDbService#FilmDbService(FilmDbStorage, UserDbStorage, GenreDao, MpaDao, LikeDao)
     */
    @Autowired
    public FilmDbService(@Qualifier("FilmDbStorage")
                             FilmDbStorage filmStorage,
                             @Qualifier("UserDbStorage") UserDbStorage userStorage,
                             GenreDao genreDao,
                             MpaDao mpaDao,
                             LikeDao likeDao) {

        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreDao = genreDao;
        this.mpaDao = mpaDao;
        this.likeDao = likeDao;
    }

    /**
     * Добавление лайка фильму.
     *
     * @param userId айди пользователя, добавляющего лайк.
     * @param filmId айди фильма, кому ставим лайк.
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id пользователя или фильма.
     */
    public void addLike(Long userId, Long filmId) {
        if (userStorage.getByIdUser(userId) == null) {
            throw new NotFoundException(String.format("Пользователь с id %s не существует", userId));
        }

        if (filmStorage.getByIdFilm(filmId) == null) {
            throw new NotFoundException(String.format("Фильм с id %s не существует", filmId));
        }

        likeDao.addLike(userId, filmId);
        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, filmId);
    }

    /**
     * Удаление лайка у фильма.
     *
     * @param userId айди пользователя, удаляющего лайк.
     * @param filmId айди фильма, у кого удаляем лайк.
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id пользователя или фильма.
     */
    public void deleteLike(Long userId, Long filmId) {
        if (userStorage.getByIdUser(userId) == null) {
            throw new NotFoundException(String.format("Пользователь с id %s не существует", userId));
        }

        if (filmStorage.getByIdFilm(filmId) == null) {
            throw new NotFoundException(String.format("Фильм с id %s не существует", filmId));
        }

        likeDao.deleteLike(userId, filmId);
        log.info("Пользователь с id {} удалил лайк у фильма с id{}", userId, filmId);
    }

    /**
     * Возвращает топ фильмов по лайкам.
     *
     * @param topNumber количество, из которого необходимо составить топ(по умолчанию топ 10).
     */

    public List<Film> getPopularFilm(int topNumber) {
        return getFilm().stream()
                .sorted(this::compare)
                .limit(topNumber)
                .collect(Collectors.toList());
    }

    /**
     * Метод создает новый фильм в БД
     *
     * @param film объект фильма
     * @return возвращает созданный фильм
     */
    public Film addFilms(Film film) {
        Validation.validationFilm(film);
        Film theFilm = filmStorage.addFilms(film);
        if (film.getGenres() != null) {
            genreDao.addGenres(theFilm.getId(), film.getGenres());
            theFilm.setGenres(filmStorage.getGenresByFilm(theFilm.getId()));
        }
        theFilm.setMpa(mpaDao.getMpaById(theFilm.getMpa().getId()));
        return theFilm;
    }

    /**
     * Метод обновляет данные в БД о фильме
     *
     * @param film объект фильма
     * @return возвращает обновленный фильм
     */
    public Film put(Film film) {
        Validation.validationFilm(film);
        Film theFilm = filmStorage.put(film);
        if (theFilm.getGenres() != null) {
            genreDao.updateGenres(theFilm.getId(), film.getGenres());
            theFilm.setGenres(filmStorage.getGenresByFilm(theFilm.getId()));
        }
        theFilm.setMpa(mpaDao.getMpaById(theFilm.getMpa().getId()));
        return theFilm;
    }

    /**
     * Метод предоставляет доступ(прокладка) к методу запроса фильмов из хранилища фильмов в виде коллекции{@link FilmDbStorage}
     *
     * @return возвращает коллекцию фильмов
     */
    public Collection<Film> getFilm() {
        Collection<Film> films = filmStorage.getFilm();
        for (Film film : films) {
            film.setGenres(filmStorage.getGenresByFilm(film.getId()));
            film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
        }
        return films;
    }

    /**
     * Метод предоставляет доступ(прокладка) к методу получения фильма из хранилища фильмов по id{@link FilmDbStorage}
     *
     * @param id идентификатор запрашиваемого фильма
     * @return возвращает объект фильма с указанным id
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id пользователя или фильма.
     */

    public Film getByIdFilm(Long id) {
        Film film;
        try {
            film = filmStorage.getByIdFilm(id);
            film.setGenres(filmStorage.getGenresByFilm(id));
            film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));

            return film;
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException(String.format("Фильма с id %s не существует", id));
        }
    }

    /**
     * Метод для определения популярности фильма(компаратор), сравнивающий значения лайков у двух фильмов.
     *
     * @param film фильм для сравнения
     * @param otherFilm второй фильм для сравнения
     */
    private int compare(Film film, Film otherFilm) {
        return Integer.compare(likeDao.examinationLikes(otherFilm.getId()), likeDao.examinationLikes(film.getId()));
    }
}
