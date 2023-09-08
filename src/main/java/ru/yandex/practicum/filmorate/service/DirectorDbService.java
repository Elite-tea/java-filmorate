package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.dao.director.DirectorDao;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.util.List;

import static java.lang.String.format;

/**
 * Класс-сервис с логикой для оперирования сущностями режиссеров <b>DirectorDao<b/>
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DirectorDbService {
    /**
     * Поле для доступа к операциям сущностей режиссеров
     */
    private final DirectorDao directorDao;

    public Director addDirector(Director director) {
        Validation.validationDirector(director);
        return directorDao.addDirector(director);
    }

    public Director updateDirector(Director director) {
        Validation.validationDirector(director);
        if (!directorDao.isContains(director.getId())) {
            throw new NotFoundException(format("Режиссер с идентификатором %d не найден", director.getId()));
        }
        return directorDao.updateDirectorData(director);
    }

    public Director getDirectorById(Integer id) {
        idValidation(id);
        return directorDao.getDirectorById(id);
    }

    public void deleteDirectorById(Integer id) {
        idValidation(id);
        directorDao.deleteDirectorById(id);
    }

    public List<Director> getDirectors() {
        return directorDao.getDirectors();
    }

    private void idValidation(Integer id) {
        if (id == null) {
            throw new NotFoundException("Передан режиссер с пустым идентификатором");
        }
        if (!directorDao.isContains(id)) {
            throw new NotFoundException(format("Режиссер с идентификатором %d не найден", id));
        }
    }
}
