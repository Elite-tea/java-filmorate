package ru.yandex.practicum.filmorate.storage.dao.genre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.util.HashSet;
import java.util.LinkedHashSet;

@Slf4j
@AllArgsConstructor
@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(Integer id) {
        log.debug("getGenreById({})", id);
        try {
            Genre genre = jdbcTemplate.queryForObject("SELECT * FROM genre WHERE genre_id = ?",
                    new GenreMapper(), id);
            log.trace("Возвращен жанр по запросу: {}", genre);
            return genre;
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException(String.format("Жанра с id %d не существует", id));
        }
    }

    @Override
    public HashSet<Genre> getGenres() {
        log.debug("getGenres()");
        HashSet<Genre> genres = new LinkedHashSet<>(jdbcTemplate.query("SELECT * FROM genre ORDER BY genre_id",
                new GenreMapper()));
        log.trace("Возвращён список жанров: {}", genres);
        return genres;
    }

    @Override
    public void addGenres(Long filmId, HashSet<Genre> genres) {
        log.debug("addGenres({}, {})", filmId, genres);
        for (Genre genre : genres) {
            jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)", filmId, genre.getId());
        }
        log.trace("Фильму с идентификатором {} были присвоены жанры: {}", filmId, genres);
    }

    public void deleteGenres(Long filmId) {
        log.debug("deleteGenres({})", filmId);
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id=?", filmId);
        log.trace("У фильма {} были удалены жанры", filmId);
    }

    @Override
    public void updateGenres(Long filmId, HashSet<Genre> genres) {
        log.debug("updateGenres({}, {})", filmId, genres);
        deleteGenres(filmId);
        addGenres(filmId, genres);
        log.trace("У фильма {} были обновлены жанры: {}", filmId, genres);
    }
}
