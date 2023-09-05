package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SortBy;
import ru.yandex.practicum.filmorate.storage.dao.director.DirectorDao;
import ru.yandex.practicum.filmorate.storage.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.storage.dao.like.LikeDao;
import ru.yandex.practicum.filmorate.storage.dao.mpa.MpaDao;
import ru.yandex.practicum.filmorate.storage.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.util.Collection;
import java.util.HashSet;
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
     * Поле для доступа к операциям с режиссерами
     */
    private final DirectorDao directorDao;

    /**
     * Конструктор сервиса.
     *
     * @see FilmDbService#FilmDbService(FilmDbStorage, UserDbStorage, GenreDao, MpaDao, LikeDao, DirectorDao)
     */
    @Autowired
    public FilmDbService(@Qualifier("FilmDbStorage") FilmDbStorage filmStorage,
                         @Qualifier("UserDbStorage") UserDbStorage userStorage,
                         GenreDao genreDao,
                         MpaDao mpaDao,
                         LikeDao likeDao,
                         DirectorDao directorDao) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreDao = genreDao;
        this.mpaDao = mpaDao;
        this.likeDao = likeDao;
        this.directorDao = directorDao;
    }

    public void addLike(Long userId, Long filmId) {
        checker(userId, filmId);
        likeDao.addLike(userId, filmId);
        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, filmId);
    }

    public void deleteLike(Long userId, Long filmId) {
        checker(userId, filmId);
        likeDao.deleteLike(userId, filmId);
        log.info("Пользователь с id {} удалил лайк у фильма с id {}", userId, filmId);
    }

    /**
     * Возвращает топ фильмов по лайкам.
     *
     * @param count количество, из которого необходимо составить топ(по умолчанию значение равно 10).
     */
    public List<Film> getPopularFilms(int count) {
        return getFilms().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film addFilm(Film film) {
        Validation.validationFilm(film);
        Film theFilm = filmStorage.addFilm(film);
        if (film.getGenres() != null) {
            genreDao.addGenres(theFilm.getId(), film.getGenres());
            theFilm.setGenres(filmStorage.getGenresByFilm(theFilm.getId()));
        }
        if (film.getDirectors() != null) {
            directorDao.addDirectorsToFilm(theFilm.getId(), film.getDirectors());
            theFilm.setDirectors(directorDao.getDirectorsByFilm(theFilm.getId()));
        }
        theFilm.setMpa(mpaDao.getMpaById(theFilm.getMpa().getId()));
        return theFilm;
    }

    public Film updateFilm(Film film) {
        Validation.validationFilm(film);
        Film theFilm = filmStorage.updateFilm(film);
        if (theFilm.getGenres() != null) {
            genreDao.updateGenres(theFilm.getId(), film.getGenres());
            theFilm.setGenres(filmStorage.getGenresByFilm(theFilm.getId()));
        } else {
            theFilm.setGenres(new HashSet<>());
        }
        if (theFilm.getDirectors() != null) {
            directorDao.updateDirectorsInFilm(theFilm.getId(), film.getDirectors());
            theFilm.setDirectors(directorDao.getDirectorsByFilm(theFilm.getId()));
        } else {
            directorDao.deleteDirectorsFromFilm(theFilm.getId());
            theFilm.setDirectors(new HashSet<>());
        }
        theFilm.setMpa(mpaDao.getMpaById(theFilm.getMpa().getId()));
        return theFilm;
    }

    public Collection<Film> getFilms() {
        Collection<Film> films = filmStorage.getFilms();
        for (Film film : films) {
            film.setGenres(filmStorage.getGenresByFilm(film.getId()));
            film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
            film.setDirectors(directorDao.getDirectorsByFilm(film.getId()));
        }
        return films;
    }

    public Film getFilmById(Long id) {
        Film film;
        try {
            film = filmStorage.getFilmById(id);
            film.setGenres(filmStorage.getGenresByFilm(id));
            film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
            film.setDirectors(directorDao.getDirectorsByFilm(film.getId()));
            return film;
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException(String.format("Фильма с id %d не существует", id));
        }
    }

    /**
     * Метод для определения популярности фильма(компаратор), сравнивающий значения лайков у двух фильмов
     *
     * @param film      фильм для сравнения
     * @param otherFilm второй фильм для сравнения
     */
    private int compare(Film film, Film otherFilm) {
        return Integer.compare(likeDao.checkLikes(otherFilm.getId()), likeDao.checkLikes(film.getId()));
    }

    public List<Film> getDirectorsFilms(Integer directorId, SortBy sortBy) {
        List<Film> filmList;
        if (directorId != null && directorDao.isContains(directorId)) {
            filmList = filmStorage.getDirectorFilms(directorId, sortBy).stream()
                    .map(film -> getFilmById(film.getId()))
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Данные о режиссере не найдены по идентификатору: " + directorId);
        }
        return filmList;
    }

    /**
     * Метод для проверки пользователя и фильма на наличие в БД с последующей оценкой фильма
     * @param userId идентификатор пользователя
     * @param filmId идентификатор фильма
     */
    private void checker(Long userId, Long filmId) {
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException(String.format("Пользователя с id %d не существует", userId));
        }

        if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException(String.format("Фильма с id %d не существует", filmId));
        }
    }
}