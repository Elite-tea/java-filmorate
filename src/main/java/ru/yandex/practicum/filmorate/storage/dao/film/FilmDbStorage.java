package ru.yandex.practicum.filmorate.storage.dao.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.SortBy;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.mapper.FilmsWithGenreMapper;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.Comparator;

@Slf4j
@Component("FilmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_POPULAR_FILM_ON_GENRES = "SELECT f.film_id AS film_id, f.name AS name, " +
            "f.description AS description, f.release_date AS release_date, f.duration AS duration, " +
            "f.mpa_id AS mpa_id, m.mpa_name AS mpa_name, fg.genre_id AS genre_id, g.genre_name AS genre_name, " +
            "fd.directors_id AS directors_id, d.directors_name AS directors_name " +
            "FROM film AS f INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "INNER JOIN film_genre AS fg ON f.film_id = fg.film_id " +
            "INNER JOIN genre AS g ON fg.genre_id = g.genre_id " +
            "LEFT JOIN film_directors fd ON f.film_id = fd.film_id " +
            "LEFT JOIN directors d ON fd.directors_id = d.directors_id " +
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
            "f.mpa_id AS mpa_id, m.mpa_name AS mpa_name, fg.genre_id AS genre_id, g.genre_name AS genre_name, " +
            "fd.directors_id AS directors_id, d.directors_name AS directors_name " +
            "FROM film AS f INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
            "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
            "LEFT JOIN film_directors fd ON f.film_id = fd.film_id " +
            "LEFT JOIN directors d ON fd.directors_id = d.directors_id " +
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
            "f.mpa_id AS mpa_id, m.mpa_name AS mpa_name, fg.genre_id AS genre_id, g.genre_name AS genre_name, " +
            "fd.directors_id AS directors_id, d.directors_name AS directors_name " +
            "FROM film AS f INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "INNER JOIN film_genre AS fg ON f.film_id = fg.film_id " +
            "INNER JOIN genre AS g ON fg.genre_id = g.genre_id " +
            "LEFT JOIN film_directors fd ON f.film_id = fd.film_id " +
            "LEFT JOIN directors d ON fd.directors_id = d.directors_id " +
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

    /**
     * Метод создает новый фильм в БД
     *
     * @param film объект фильма
     * @return возвращает созданный фильм
     */
    @Override
    public Film addFilm(Film film) {
        log.debug("addFilm({})", film);
        jdbcTemplate.update(
                "INSERT INTO film (name, description, release_date, duration, mpa_id) " +
                        "VALUES (?,?,?,?,?)", film.getName(), film.getDescription(),
                Date.valueOf(film.getReleaseDate()), film.getDuration(), film.getMpa().getId());
        Film theFilm = jdbcTemplate.queryForObject(
                    "SELECT film_id, name, description, release_date, duration, mpa_id FROM film " +
                            "WHERE name=? AND description=? AND release_date=? AND duration=? AND mpa_id=? ",
                new FilmMapper(), film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                film.getDuration(), film.getMpa().getId());
        log.trace("Добавлен новый фильм в базу данных: {}", theFilm);
        return theFilm;
    }

    /**
     * Метод обновляет данные в БД о фильме
     *
     * @param film объект фильма
     * @return возвращает обновленный фильм
     */
    @Override
    public Film updateFilm(Film film) {
        log.debug("updateFilm({})", film);
        Long filmId = film.getId();
        try {
            if (!getFilmById(filmId).getName().isEmpty()) {
                jdbcTemplate.update("UPDATE film SET name=?, description=?, release_date=?, duration=?, mpa_id=? " +
                                "WHERE film_id = ?", film.getName(), film.getDescription(),
                        Date.valueOf(film.getReleaseDate()), film.getDuration(), film.getMpa().getId(), film.getId());
                log.debug("Фильм обновлен");
            }
        } catch (EmptyResultDataAccessException exception) {
            log.debug("Фильм не существует");
            throw new NotFoundException(String.format("Фильма с id %d не существует", filmId));
        }
        log.trace("Обновлён фильм : {}", film);
        return film;
    }

    /**
     * Метод предоставляет доступ(прокладка) к методу запроса фильмов из хранилища фильмов
     * в виде коллекции{@link FilmDbStorage}
     *
     * @return возвращает коллекцию фильмов
     */
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
    public Collection<Film> getFilms() {
        log.debug("getFilms()");
        Collection<Film> films = jdbcTemplate.query("SELECT * FROM film", new FilmMapper());
        log.trace("Возвращен список фильмов: {}", films);
        return films;
    }

    /**
     * Метод предоставляет доступ(прокладка) к методу получения фильма из хранилища фильмов по id{@link FilmDbStorage}
     *
     * @param id идентификатор запрашиваемого фильма
     * @return возвращает объект фильма с указанным id
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id пользователя или фильма.
     */
    @Override
    public Film getFilmById(Long id) {
        log.debug("getFilmById({})", id);
        Film film = jdbcTemplate.queryForObject("SELECT * FROM film WHERE film_id = ?", new FilmMapper(), id);
        log.trace("Возвращён фильм из базы данных: {}", film);
        return film;
    }

    @Override
    public HashSet<Genre> getGenresByFilm(Long filmId) {
        log.debug("getGenresByFilm({})", filmId);
        HashSet<Genre> genres = new HashSet<>(jdbcTemplate.query("SELECT f.genre_id, g.genre_name " +
                        "FROM film_genre AS f LEFT OUTER JOIN genre AS g ON f.genre_id = g.genre_id WHERE f.film_id=? " +
                        "ORDER BY g.genre_id", new GenreMapper(), filmId));
        log.trace("Возвращен список жанров для фильма {}: {}", filmId, genres);
        return genres;
    }

    @Override
    public List<Film> getDirectorFilms(Integer directorId, SortBy sortBy) {
        log.debug("getDirectorFilms({}, {})", directorId, sortBy);
        String year = "SELECT f.* FROM film_directors AS fd LEFT JOIN film AS f ON f.film_id = fd.film_id " +
                "WHERE director_id=? ORDER BY year(f.release_date)";
        String likes = "SELECT *, (SELECT COUNT(*) FROM likes WHERE fd.film_id = likes.film_id) AS likes " +
                "FROM film_directors AS fd LEFT JOIN film AS f ON f.film_id = fd.film_id WHERE director_id=? " +
                "ORDER BY likes DESC";
        List<Film> sortedFilms = new ArrayList<>();
        if (sortBy == SortBy.LIKES) {
            sortedFilms = jdbcTemplate.query(likes, new FilmMapper(), directorId);
            log.trace("Возвращён отсортированный список по лайкам, идентификатор режиссера {}: {}",
                    directorId, sortedFilms);
        }
        if (sortBy == SortBy.YEAR) {
            sortedFilms = jdbcTemplate.query(year, new FilmMapper(), directorId);
            log.trace("Возвращён отсортированный список по годам, идентификатор режиссера {}: {}",
                    directorId, sortedFilms);
        }
        return sortedFilms;
    }

    @Override
    public Collection<Film> getFilmsByUser(Long id) {
        return jdbcTemplate
                .query("SELECT * FROM film WHERE film_id IN (SELECT film_id FROM likes WHERE user_id = ?)",
                        new FilmMapper(), id);
    }

    @Override
    public List<Film> getPopularFilmsByGenre(int count, int genreId) {
        try {
            List<Film> films = jdbcTemplate.queryForObject(SELECT_POPULAR_FILM_ON_GENRES, new FilmsWithGenreMapper(),
                    genreId, count);
            sortingGenreInFilmOnId(films);

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
            sortingGenreInFilmOnId(films);
            return films;
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Film> getPopularFilmsByGenreAndYear(int count, int genreId, int year) {
        try {
            List<Film> films = jdbcTemplate.queryForObject(SELECT_POPULAR_FILM_ON_GENRES_AND_YEAR,
                    new FilmsWithGenreMapper(), genreId, year, count);
            sortingGenreInFilmOnId(films);
            return films;
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    private void sortingGenreInFilmOnId(List<Film> films) {
        for (Film f: films) {
            List<Genre> filmGenresNew = new ArrayList<>(f.getGenres());
            filmGenresNew.sort(Comparator.comparing(Genre::getId));
            f.getGenres().clear();
            f.getGenres().addAll(filmGenresNew);
        }
    }

    @Override
    public List<Film> getSearchResult(String query, String by) {
        String[] param = by.split(",");
        if (param.length > 1) {
            List<Film> result = jdbcTemplate.query("SELECT * FROM film WHERE LOWER(name) LIKE LOWER('%" + query + "%')", new FilmMapper());
            result.addAll(jdbcTemplate.query("SELECT * FROM film WHERE film_id IN " +
                    "(SELECT film_id FROM film_directors WHERE director_id IN " +
                    "(SELECT director_id FROM directors WHERE LOWER(director_name) LIKE LOWER('%" + query + "%')))", new FilmMapper()));
            return result;
        } else {
            if (param[0].equals("title")) {
                return jdbcTemplate.query("SELECT * FROM film WHERE LOWER(name) LIKE LOWER('%" + query + "%')", new FilmMapper());
            } else {
                return jdbcTemplate.query("SELECT * FROM film WHERE film_id IN " +
                        "(SELECT film_id FROM film_directors WHERE director_id IN " +
                        "(SELECT director_id FROM directors WHERE LOWER(director_name) LIKE LOWER('%" + query + "%')))", new FilmMapper());
            }
        }
    }
}
