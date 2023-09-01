package ru.yandex.practicum.filmorate.storage.dao.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.sql.Date;
import java.util.Collection;
import java.util.HashSet;

@Slf4j
@Component("FilmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilms(Film film) {
        jdbcTemplate.update(
                "INSERT INTO film (name, description, release_date, duration, mpa_id) VALUES (?,?,?,?,?)",
                 film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()), film.getDuration(),
                film.getMpa().getId());

        return jdbcTemplate.queryForObject(
                "SELECT film_id, name, description, release_date, duration, mpa_id FROM film " +
                 "WHERE name=? AND description=? AND release_date=? AND duration=? AND mpa_id=?",
                 new FilmMapper(), film.getName(), film.getDescription(),
                 Date.valueOf(film.getReleaseDate()), film.getDuration(), film.getMpa().getId());
    }

    @Override
    public Film put(Film film) {
        Long filmId = film.getId();
        try {
            if (!getByIdFilm(filmId).getName().isEmpty()) {
                jdbcTemplate.update(
                        "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?," +
                                "mpa_id = ? WHERE film_id = ?",
                        film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                        film.getDuration(), film.getMpa().getId(), film.getId());
                log.debug("Фильм обновлен");
            }
        } catch (EmptyResultDataAccessException exception) {
            log.debug("Фильм не существует");
            throw new NotFoundException(String.format("Фильма с id %d не существует", filmId));
        }
        return film;
    }

    @Override
    public void deleteFilm(Long id) {
        try {
            jdbcTemplate.update("DELETE FROM film WHERE film_id = ?",id);
            log.debug("Фильм удален");
        } catch (NotFoundException exception) {
            log.debug("Фильм не существует");
            throw new NotFoundException(String.format("Пользователя с id %d не существует", id));
        }
    }

    @Override
    public Collection<Film> getFilm() {
        return jdbcTemplate.query("SELECT * FROM film", new FilmMapper());
    }

    @Override
    public Film getByIdFilm(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM film WHERE film_id = ?", new FilmMapper(), id);
    }

    @Override
    public HashSet<Genre> getGenresByFilm(Long filmId) {
        return new HashSet<>(jdbcTemplate.query("SELECT f.genre_id, g.genre_name FROM film_genre AS f " +
                "LEFT OUTER JOIN genre AS g ON f.genre_id = g.genre_id WHERE f.film_id=? ORDER BY g.genre_id",
                new GenreMapper(), filmId));
    }
}
