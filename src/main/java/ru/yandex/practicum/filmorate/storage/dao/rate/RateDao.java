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
     */
    void rateFilm(Long userId, Long filmId, Integer rate);
    /**
     * Обновление оценки фильму от пользователя
     *
     * @param userId идентификатор пользователя, добавляющего оценку
     * @param filmId идентификатор фильма, которому обновляют оценку
     * @param rate оценка, передаваемая пользователю фильму
     * @throws NotFoundException генерирует ошибку, если не найден объект rate
     */

    void updateFilmRate(Long userId, Long filmId, Integer rate);
    /**
     * Удаление оценки фильма от пользователя
     *
     * @param userId идентификатор пользователя, удаляющего оценку
     * @param filmId идентификатор фильма, которому удаляют оценку
     */

    void deleteFilmRate(Long userId, Long filmId);
    /**
     * Проверяет оценки методом подсчета, и подготавливает данные для компаратора
     *
     * @param filmId идентификатор фильма для подсчета оценок
     */

    double checkRates(Long filmId);
}
