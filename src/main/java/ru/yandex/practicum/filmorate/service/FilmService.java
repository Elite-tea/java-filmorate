package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    /**
     * Добавление лайка фильму.
     *
     * @param userId айди пользователя, добавляющего лайк.
     * @param filmId айди фильма, кому ставим лайк.
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id пользователя или фильма.
     */

    public void addLike(Long userId, Long filmId) {
        if (userStorage.getByIdUser(userId) != null) {
            if (filmStorage.getByIdFilm(filmId) != null) {
                filmStorage.getByIdFilm(filmId).addLike(userId);

                log.info("Пользователь {} поставил лайк фильму {}", userStorage.getByIdUser(userId), filmStorage.getByIdFilm(filmId).getName());
            } else {
                throw new NotFoundException(String.format("Фильм с id %s не существует", filmId));
            }
        } else {
            throw new NotFoundException(String.format("Пользователь с id %s не существует", userId));
        }

    }

    /**
     * Удаление лайка у фильма.
     *
     * @param userId айди пользователя, удаляющего лайк.
     * @param filmId айди фильма, у кого удаляем лайк.
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id пользователя или фильма.
     */

    public void deleteLike(Long userId, Long filmId) {
        if (userStorage.getByIdUser(userId) != null) {
            if (filmStorage.getByIdFilm(filmId) != null) {
                filmStorage.getByIdFilm(filmId).deleteLike(userId);

                log.info("Пользователь {} удалил лайк фильма {}", userStorage.getByIdUser(userId), filmStorage.getByIdFilm(filmId).getName());
            } else {
                throw new NotFoundException(String.format("Фильм с id %s не существует", filmId));
            }
        } else {
            throw new NotFoundException(String.format("Пользователь с id %s не существует", userId));
        }
    }

    /**
     * Возвращает топ фильмов по лайкам.
     *
     * @param topNumber количество, из которого необходимо составить топ(по умолчанию топ 10).
     */

    public List<Film> getPopularFilm(int topNumber) {
        return filmStorage.getFilm().stream().sorted(Comparator.comparingInt(Film::getLike).reversed()).limit(topNumber).collect(Collectors.toList());
    }
}