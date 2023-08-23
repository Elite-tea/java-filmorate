package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeDao {
    /**
     * Добавление лайка фильму.
     *
     * @param userId айди пользователя, добавляющего лайк.
     * @param filmId айди фильма, кому ставим лайк.
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id пользователя или фильма.
     */
    void addLike(Long userId, Long filmId);

    /**
     * Удаление лайка у фильма.
     *
     * @param userId айди пользователя, удаляющего лайк.
     * @param filmId айди фильма, у кого удаляем лайк.
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id пользователя или фильма.
     */
    void deleteLike(Long userId, Long filmId);

    /**
     * Возвращает топ фильмов по лайкам.
     *
     * @param topNumber количество, из которого необходимо составить топ(по умолчанию топ 10).
     */

    List<Film> getPopularFilm(int topNumber);

    public int examinationLikes(Long filmId);
}
