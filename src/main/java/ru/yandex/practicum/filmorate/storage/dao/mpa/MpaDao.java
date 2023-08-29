package ru.yandex.practicum.filmorate.storage.dao.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

/**
 * Интерфейс для работы с логикой касающиеся лайков, реализован в {@link MpaDaoImpl}
 */
public interface MpaDao {
    /**
     * Запрос рейтинга по идентификатору
     *
     * @param id id пользователя, добавляющего лайк.
     */
    Mpa getMpaById(Integer id);

    /**
     * Запрос списка рейтингов.
     *
     * @return Возвращает список рейтингов.
     */
    List<Mpa> getListMpa();
}
