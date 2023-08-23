package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreDbService {
    private final GenreDao genreDao;

    public Genre getGenreById(Integer id) {
        if (id == null) {
            throw new NotFoundException("Жанра не существует");
        }
        return genreDao.getGenreById(id);
    }

    public Collection<Genre> getGenres() {
        return genreDao.getGenres();
    }

}
