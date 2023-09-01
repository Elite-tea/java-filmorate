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
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.mapper.FilmsWithGenreMapper;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component("FilmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_POPULAR_FILM_ON_GENRES = "SELECT f.film_id AS film_id, f.name AS name, " +
            "f.description AS description, f.release_date AS release_date, f.duration AS duration, " +
            "f.mpa_id AS mpa_id, m.mpa_name AS mpa_name, fg.genre_id AS genre_id, g.genre_name AS genre_name " +
            "FROM film AS f INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "INNER JOIN film_genre AS fg ON f.film_id = fg.film_id " +
            "INNER JOIN genre AS g ON fg.genre_id = g.genre_id " +
            "WHERE f.film_id IN (" +
                "SELECT id FROM (" +
                    "SELECT f.film_id AS id, l.user_id " +
                    "FROM film AS f " +
                    "INNER JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                    "INNER JOIN genre g ON fg.genre_id = g.genre_id " +
                    "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                    "WHERE g.genre_id = ? " +
                    "GROUP BY f.film_id " +
                    "ORDER BY count(l.user_id) DESC, f.film_id ASC " +
                    "LIMIT ?) " +
                ")";

    private static final String SELECT_POPULAR_FILM_ON_YEAR = "SELECT f.film_id AS film_id, f.name AS name, " +
            "f.description AS description, f.release_date AS release_date, f.duration AS duration, " +
            "f.mpa_id AS mpa_id, m.mpa_name AS mpa_name, fg.genre_id AS genre_id, g.genre_name AS genre_name " +
            "FROM film AS f INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
            "INNER JOIN genre AS g ON fg.genre_id = g.genre_id " +
            "WHERE f.film_id IN (" +
            "SELECT id FROM (" +
            "SELECT f.film_id AS id, l.user_id " +
            "FROM film AS f " +
            "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
            "WHERE EXTRACT(YEAR FROM CAST(f.release_date AS date)) = ? " +
            "GROUP BY f.film_id " +
            "ORDER BY count(l.user_id) DESC, f.film_id ASC " +
            "LIMIT ?) " +
            ")";

    private static final String SELECT_POPULAR_FILM_ON_GENRES_AND_YEAR = "SELECT f.film_id AS film_id, f.name AS name, " +
            "f.description AS description, f.release_date AS release_date, f.duration AS duration, " +
            "f.mpa_id AS mpa_id, m.mpa_name AS mpa_name, fg.genre_id AS genre_id, g.genre_name AS genre_name " +
            "FROM film AS f INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "INNER JOIN film_genre AS fg ON f.film_id = fg.film_id " +
            "INNER JOIN genre AS g ON fg.genre_id = g.genre_id " +
            "WHERE f.film_id IN (" +
            "SELECT id FROM (" +
            "SELECT f.film_id AS id, l.user_id " +
            "FROM film AS f " +
            "INNER JOIN film_genre AS fg ON f.film_id = fg.film_id " +
            "INNER JOIN genre g ON fg.genre_id = g.genre_id " +
            "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
            "WHERE g.genre_id = ? " +
            "AND EXTRACT(YEAR FROM CAST(f.release_date AS date)) = ? " +
            "GROUP BY f.film_id " +
            "ORDER BY count(l.user_id) DESC, f.film_id ASC " +
            "LIMIT ?) " +
            ")";

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

            }
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException(String.format("Фильма с id %d не существует", filmId));
        }
        return film;
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

    @Override
    public List<Film> getPopularFilmsByGenry(int count, int genreId) {
        try {
            List<Film> films = jdbcTemplate.queryForObject(SELECT_POPULAR_FILM_ON_GENRES, new FilmsWithGenreMapper(),
                    genreId, count);
            for (Film f: films) {
                List<Genre> filmGenresNew = new ArrayList<>(f.getGenres());
                filmGenresNew.sort(Comparator.comparing(Genre::getId));
                f.getGenres().clear();
                f.getGenres().addAll(filmGenresNew);
            }
            return films;
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Film> getPopularFilmsByYear(int count, int year) {
        try {
            List<Film> films = jdbcTemplate.queryForObject(SELECT_POPULAR_FILM_ON_YEAR, new FilmsWithGenreMapper(),
                    year, count);
            for (Film f: films) {
                List<Genre> filmGenresNew = new ArrayList<>(f.getGenres());
                filmGenresNew.sort(Comparator.comparing(Genre::getId));
                f.getGenres().clear();
                f.getGenres().addAll(filmGenresNew);
            }
            return films;
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Film> getPopularFilmsByGenryAndYear(int count, int genreId, int year) {
        try {
            List<Film> films = jdbcTemplate.queryForObject(SELECT_POPULAR_FILM_ON_GENRES_AND_YEAR,
                    new FilmsWithGenreMapper(), genreId, year, count);
            for (Film f: films) {
                List<Genre> filmGenresNew = new ArrayList<>(f.getGenres());
                filmGenresNew.sort(Comparator.comparing(Genre::getId));
                f.getGenres().clear();
                f.getGenres().addAll(filmGenresNew);
            }
            return films;
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    private void sortingGenryInFilmOnId(List<Film> films) {
        for (Film f: films) {
            List<Genre> filmGenresNew = new ArrayList<>(f.getGenres());
            filmGenresNew.sort(Comparator.comparing(Genre::getId));
            f.getGenres().clear();
            f.getGenres().addAll(filmGenresNew);
        }
    }
}
