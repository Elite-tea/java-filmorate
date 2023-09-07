package ru.yandex.practicum.filmorate.storage.dao.rate;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

/**
 * Интерфейс для работы с логикой касающиеся оценок, реализован в {@link RateDaoImpl}
 */
public interface RateDao {
    /**
     * Добавление оценки фильму от пользователя
     *
     * @param userId id пользователя, добавляющего лайк
     * @param filmId id фильма, кому ставим лайк
     * @param rate оценка, передаваемая пользователем фильму
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id пользователя или фильма
     */
    void rateFilm(Long userId, Long filmId, Integer rate);

    /**
     * Проверяет оценки методом подсчета, и подготавливает данные для компаратора
     *
     * @param filmId идентификатор фильма для подсчета оценок
     */
    double checkRates(Long filmId);
}
