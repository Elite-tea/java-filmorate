package ru.yandex.practicum.filmorate.storage.dao.like;

import ru.yandex.practicum.filmorate.Exception.NotFoundException;

/**
 * Интерфейс для работы с логикой касающиеся лайков, реализован в {@link LikeDaoImpl}
 */
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
     * Проверяет лайки методом подсчета, и подготавливает данные для компаратора
     *
     * @param filmId идентификатор фильма для подсчета лайков.
     */
    int checkLikes(Long filmId);
}
