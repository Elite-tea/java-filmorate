package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.mpa.MpaDao;

import java.util.Collection;

/**
 * Класс-сервис с логикой для оперирования рейтингом
 */
@Service
@RequiredArgsConstructor
public class MpaDbService {
    /**
     * Поле для доступа к операциям с рейтингом
     */
    private final MpaDao mpaDao;

    /**
     * Метод получает рейтинг по его идентификатору.
     *
     * @param id идентификатор запрашиваемого рейтинга
     * @return возвращает объект рейтинга с указанным id
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id жанра.
     */
    public Mpa getMpaById(Integer id) {
        try {
            return mpaDao.getMpaById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException(String.format("Рейтинг с id %s не существует", id));
        }
    }

    /**
     * Метод получает коллекция рейтинга.
     *
     * @return возвращает коллекцию рейтинга.
     */
    public Collection<Mpa> getListMpa() {
        return mpaDao.getListMpa();
    }
}
