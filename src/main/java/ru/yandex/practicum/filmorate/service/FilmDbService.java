package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.assistant.EventType;
import ru.yandex.practicum.filmorate.assistant.Operation;
import ru.yandex.practicum.filmorate.assistant.SortBy;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.director.DirectorDao;
import ru.yandex.practicum.filmorate.storage.dao.feed.FeedStorage;
import ru.yandex.practicum.filmorate.storage.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.storage.dao.rate.RateDao;
import ru.yandex.practicum.filmorate.storage.dao.mpa.MpaDao;
import ru.yandex.practicum.filmorate.storage.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс-сервис с логикой для оперирования фильмами с хранилищами <b>filmDbStorage<b/> и <b>userDbStorage<b/>
 */
@Getter
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
    private final RateDao rateDao;
    /**
     * Поле для доступа к операциям с режиссерами
     */
    private final DirectorDao directorDao;

    /**
     * Поле для доступа к операциям с лентой событий.
     */
    private final FeedStorage feedStorage;

    /**
     * Конструктор сервиса.
     *
     * @see FilmDbService#FilmDbService(FilmDbStorage, UserDbStorage, GenreDao, MpaDao, RateDao, DirectorDao, FeedStorage)
     */
    @Autowired
    public FilmDbService(@Qualifier("FilmDbStorage") FilmDbStorage filmStorage,
                         @Qualifier("UserDbStorage") UserDbStorage userStorage,
                         GenreDao genreDao,
                         MpaDao mpaDao,
                         RateDao rateDao,
                         DirectorDao directorDao,
                         FeedStorage feedStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreDao = genreDao;
        this.mpaDao = mpaDao;
        this.rateDao = rateDao;
        this.directorDao = directorDao;
        this.feedStorage = feedStorage;
    }

    public void rateFilm(Long filmId, Long userId, Integer rate) {
        checker(userId, filmId);
        rateDao.rateFilm(filmId, userId, rate);
        feedStorage.addFeed(LocalDateTime.now(), userId, EventType.RATE, Operation.ADD, filmId);
    }

    public void updateFilmRate(Long filmId, Long userId, Integer rate) {
        checker(userId, filmId);
        rateDao.updateFilmRate(filmId, userId, rate);
        feedStorage.addFeed(LocalDateTime.now(), userId, EventType.RATE, Operation.UPDATE, filmId);
    }

    public void deleteFilmRate(Long filmId, Long userId) {
        checker(userId, filmId);
        rateDao.deleteFilmRate(userId, filmId);
        feedStorage.addFeed(LocalDateTime.now(), userId, EventType.RATE, Operation.REMOVE, filmId);
    }

    /**
     * Возвращает топ фильмов по лайкам или по жанру, по году релиза фильма или жанру и году сразу.
     *
     * @param count количество, из которого необходимо составить топ(по умолчанию значение равно 10).
     * @param genreId идентификатор жанра.
     * @param year год.
     */
    public List<Film> getPopularFilms(int count, Optional<Integer> genreId, Optional<Integer> year) {
        List<Film> resultFilms;
        if (genreId.isEmpty() && year.isEmpty()) {
            log.info("Запрос популярных фильмов с параметром - колличество {}.", count);
            return getFilms().stream()
                    .sorted(this::compare)
                    .limit(count)
                    .collect(Collectors.toList());
        } else if (year.isEmpty()) {
            log.info("Запрос популярных фильмов с параметрами: колличество {}, жанр  {}", count, genreId.get());
            genreDao.getGenreById(genreId.get());
            resultFilms = filmStorage.getPopularFilmsByGenre(count, genreId.get()).stream()
                .sorted(this::compare)
                .collect(Collectors.toList());
            for(Film f: resultFilms) {
                f.setRate(rateDao.checkRates(f.getId()));
            }
            return resultFilms;
        } else if (genreId.isEmpty()) {
            log.info("Запрос популярных фильмов с параметрами: колличество {}, год  {}", count, year.get());
            resultFilms = filmStorage.getPopularFilmsByYear(count, year.get());
            for(Film f: resultFilms) {
                f.setRate(rateDao.checkRates(f.getId()));
            }
            return resultFilms;
        } else {
            log.info("Запрос популярных фильмов с параметрами: колличество {}, жанр  {}, год  {}",
                count, genreId.get(), year.get());
            genreDao.getGenreById(genreId.get());
            resultFilms = filmStorage.getPopularFilmsByGenreAndYear(count, genreId.get(), year.get());
            for(Film f: resultFilms) {
                f.setRate(rateDao.checkRates(f.getId()));
            }
            return resultFilms;
        }
    }

    /**
     * Возвращает список общих фильмов.
     *
     * @param userId   идентификатор пользователя, запрашивающего информацию
     * @param friendId идентификатор пользователя, с которым необходимо сравнить список фильмов
     * @return возвращает список общих с другом фильмов с сортировкой по их популярности
     */
    public List<Film> getCommonFilms(Long userId, Long friendId) {
        Collection<Film> listOfUserFilms = getFilmsByUser(userId);
        Collection<Film> listOfFriendFilms = getFilmsByUser(friendId);
        Set<Film> commonList = new HashSet<>(listOfUserFilms);
        commonList.retainAll(listOfFriendFilms);
        return new ArrayList<>(commonList);

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
        // установка оценки фильму и выявление среднего арифметического вместо метода подсчёта лайков
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
        // установка оценки фильму и выявление среднего арифметического вместо метода подсчёта лайков
        theFilm.setMpa(mpaDao.getMpaById(theFilm.getMpa().getId()));
        return theFilm;
    }

    /**
     * Метод запроса коллекции всех фильмов
     *
     * @return возвращает коллекцию фильмов
     */
    public Collection<Film> getFilms() {
        Collection<Film> films = filmStorage.getFilms();
        for (Film film : films) {
            film.setGenres(filmStorage.getGenresByFilm(film.getId()));
            film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
            film.setDirectors(directorDao.getDirectorsByFilm(film.getId()));
            film.setRate(rateDao.checkRates(film.getId()));
        }
        return films;
    }

    /**
     * Метод запроса фильма по id
     *
     * @param id идентификатор запрашиваемого фильма
     * @return возвращает объект фильма с указанным id
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id пользователя или фильма.
     */
    public Film getFilmById(Long id) {
        Film film;
        try {
            film = filmStorage.getFilmById(id);
            film.setGenres(filmStorage.getGenresByFilm(id));
            film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
            film.setDirectors(directorDao.getDirectorsByFilm(film.getId()));
            // установка оценки фильму и выявление среднего арифметического вместо метода подсчёта лайков
            return film;
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException(String.format("Фильма с id %d не существует", id));
        }
    }

    /**
     * Метод предоставляет список фильмов которые понравились пользователю. Метод-помощник для сервиса пользователей.
     * Перед использованием необходимо осуществить проверку регистрации пользователя в сервисе.
     *
     * @param id id пользователя для которого выгружаются понравившиеся фильмы.
     * @return возвращает список понравившихся фильмов.
     */
    public Collection<Film> getFilmsByUser(Long id) {
        Collection<Film> films = filmStorage.getFilmsByUser(id);
        for (Film film : films) {
            film.setGenres(filmStorage.getGenresByFilm(film.getId()));
            film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
            film.setRate(rateDao.checkRates(film.getId()));
        }
        return films;
    }

    /**
     * Метод для определения популярности фильма(компаратор), сравнивающий значения лайков у двух фильмов.
     *
     * @param film      фильм для сравнения
     * @param otherFilm второй фильм для сравнения
     */
    private int compare(Film film, Film otherFilm) {
        return Double.compare(rateDao.checkRates(otherFilm.getId()), rateDao.checkRates(film.getId()));
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
     *
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

    /**
     * поиск по названию фильмов и по режиссёру
     *
     * @param query — текст для поиска,
     * @param by    — может принимать значения director (поиск по режиссёру), title (поиск по названию),
     *              либо оба значения через запятую при поиске одновременно и по режиссеру и по названию.
     * @return возвращает список фильмов с количеством лайков (От большего к меньшему)
     */
    public List<Film> getSearchResult(String query, String by) {
        List<Film> listOfFilm = filmStorage.getSearchResult(query, by);
        for (Film film : listOfFilm) {
            film.setGenres(filmStorage.getGenresByFilm(film.getId()));
            film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
            film.setDirectors(directorDao.getDirectorsByFilm(film.getId()));
        }
        return listOfFilm.stream()
                .sorted(this::compare)
                .collect(Collectors.toList());
    }
}