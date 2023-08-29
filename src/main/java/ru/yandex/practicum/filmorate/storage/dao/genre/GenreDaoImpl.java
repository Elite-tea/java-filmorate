package ru.yandex.practicum.filmorate.storage.dao.genre;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.util.HashSet;
import java.util.LinkedHashSet;

@AllArgsConstructor
@Component
public class GenreDaoImpl implements GenreDao {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(Integer id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM genre WHERE genre_id = ?", new GenreMapper(), id);
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException(String.format("Жанра с id %s не существует", id));
        }
    }

    @Override
    public HashSet<Genre> getGenres() {
        return new LinkedHashSet<>(jdbcTemplate.query("SELECT * FROM genre ORDER BY genre_id", new GenreMapper()));
    }

    @Override
    public void addGenres(Long filmId, HashSet<Genre> genres) {
        for (Genre genre : genres) {
            jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)", filmId, genre.getId());
        }
    }

    public void deleteGenres(Long filmId) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id=?", filmId);
    }

    @Override
    public void updateGenres(Long filmId, HashSet<Genre> genres) {
        deleteGenres(filmId);
        addGenres(filmId, genres);
    }
}
