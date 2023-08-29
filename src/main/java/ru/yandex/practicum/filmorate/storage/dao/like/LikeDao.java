package ru.yandex.practicum.filmorate.storage.dao.like;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

/**
 * Интерфейс для работы с логикой касающиеся лайков, реализован в {@link LikeDaoImpl}
 */
public interface LikeDao {
    /**
     * Добавление лайка фильму.
     *
     * @param userId id пользователя, добавляющего лайк.
     * @param filmId id фильма, кому ставим лайк.
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id пользователя или фильма.
     */
    void addLike(Long userId, Long filmId);

    /**
     * Удаление лайка у фильма.
     *
     * @param userId id пользователя, удаляющего лайк.
     * @param filmId id фильма, у кого удаляем лайк.
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
