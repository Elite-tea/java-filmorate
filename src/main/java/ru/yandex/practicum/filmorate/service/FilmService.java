//package ru.yandex.practicum.filmorate.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import ru.yandex.practicum.filmorate.Exception.NotFoundException;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.storage.dao.GenreDao;
//import ru.yandex.practicum.filmorate.storage.dao.LikeDao;
//import ru.yandex.practicum.filmorate.storage.dao.MpaDao;
//import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
//import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
//import ru.yandex.practicum.filmorate.storage.user.UserStorage;
//
//import java.util.Collection;
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * Класс-сервис с логикой для оперирования лайками к фильму с хранилищами <b>filmStorage<b/> и <b>userStorage<b/>
// */
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class FilmService {
//    /**
//     * Поле хранилище фильмов
//     */
//    private final FilmStorage filmStorage;
//    /**
//     * Поле хранилище пользователей
//     */
//    private final UserStorage userStorage;
//
//    /**
//     * Добавление лайка фильму.
//     *
//     * @param userId айди пользователя, добавляющего лайк.
//     * @param filmId айди фильма, кому ставим лайк.
//     * @throws NotFoundException генерирует ошибку 404 если введен не верный id пользователя или фильма.
//     */
//    public void addLike(Long userId, Long filmId) {
//        if (userStorage.getByIdUser(userId) == null) {
//            throw new NotFoundException(String.format("Пользователь с id %s не существует", userId));
//        }
//
//        if (filmStorage.getByIdFilm(filmId) == null) {
//            throw new NotFoundException(String.format("Фильм с id %s не существует", filmId));
//        }
//
//        filmStorage.getByIdFilm(filmId).addLike(userId);
//        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, filmId);
//    }
//
//    /**
//     * Удаление лайка у фильма.
//     *
//     * @param userId айди пользователя, удаляющего лайк.
//     * @param filmId айди фильма, у кого удаляем лайк.
//     * @throws NotFoundException генерирует ошибку 404 если введен не верный id пользователя или фильма.
//     */
//    public void deleteLike(Long userId, Long filmId) {
//        if (userStorage.getByIdUser(userId) == null) {
//            throw new NotFoundException(String.format("Пользователь с id %s не существует", userId));
//        }
//
//        if (filmStorage.getByIdFilm(filmId) == null) {
//            throw new NotFoundException(String.format("Фильм с id %s не существует", filmId));
//        }
//
//        filmStorage.getByIdFilm(filmId).deleteLike(userId);
//        log.info("Пользователь с id {} удалил лайк у фильма с id{}", userId, filmId);
//    }
//
//    /**
//     * Возвращает топ фильмов по лайкам.
//     *
//     * @param topNumber количество, из которого необходимо составить топ(по умолчанию топ 10).
//     */
//
//    public List<Film> getPopularFilm(int topNumber) {
//        return filmStorage.getFilm().stream().sorted(Comparator.comparingInt(Film::getLike).reversed()).limit(topNumber).collect(Collectors.toList());
//    }
//
//    /**
//     * Метод предоставляет доступ(прокладка) к методу добавления фильма в хранилище фильмов{@link InMemoryFilmStorage}
//     *
//     * @param film объект фильма
//     * @return возвращает созданный фильм
//     */
//    public Film addFilms(Film film) {
//        return filmStorage.addFilms(film);
//    }
//
//    /**
//     * Метод предоставляет доступ(прокладка) к методу обновления фильма в хранилище фильмов{@link InMemoryFilmStorage}
//     *
//     * @param film объект фильма
//     * @return возвращает обновленный фильм
//     */
//    public Film put(Film film) {
//        return filmStorage.put(film);
//    }
//
//    /**
//     * Метод предоставляет доступ(прокладка) к методу запроса фильмов из хранилища фильмов в виде коллекции{@link InMemoryFilmStorage}
//     *
//     * @return возвращает коллекцию фильмов
//     */
//    public Collection<Film> getFilm() {
//        return filmStorage.getFilm();
//    }
//
//    /**
//     * Метод предоставляет доступ(прокладка) к методу получения фильма из хранилища фильмов по id{@link InMemoryFilmStorage}
//     *
//     * @param id идентификатор запрашиваемого фильма
//     * @return возвращает объект фильма с указанным id
//     */
//    public Film getByIdFilm(Long id) {
//        return filmStorage.getByIdFilm(id);
//    }
//}