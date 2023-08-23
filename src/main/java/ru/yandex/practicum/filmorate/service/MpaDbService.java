package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaDbService {
    private final MpaDao mpaDao;

    public Mpa getMpaById(Integer id) {
        try {
            return mpaDao.getMpaById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException(String.format("Рейтинг с id %s не существует", id));
        }
    }

    public Collection<Mpa> getListMpa() {
        return mpaDao.getListMpa();
    }
}
