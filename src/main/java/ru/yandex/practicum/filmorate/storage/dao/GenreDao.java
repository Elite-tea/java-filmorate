package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.Set;

public interface GenreDao {
    Genre getGenreById(Integer id);

    Set<Genre> getGenres();

    void addGenres(Long filmId, HashSet<Genre> genres);

    void updateGenres(Long filmId, HashSet<Genre> genres);

}
