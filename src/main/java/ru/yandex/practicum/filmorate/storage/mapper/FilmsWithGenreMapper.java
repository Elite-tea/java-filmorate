package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Set;

public class FilmsWithGenreMapper implements RowMapper<List<Film>> {
    public List<Film> mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        HashMap<Long, Film> filmsMap = new HashMap<>();
        do {
            Film film = new Film();
            film.setId(resultSet.getLong("film_id"));
            film.setName(resultSet.getString("name"));
            film.setDescription(resultSet.getString("description"));
            film.setDuration(resultSet.getInt("duration"));
            film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
            film.setMpa(new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa_name")));
            if (!filmsMap.containsKey(film.getId())) {
                filmsMap.put(film.getId(), film);
            }
            if (resultSet.getInt("genre_id") > 0) {
                Genre genre = new Genre();
                genre.setId(resultSet.getInt("genre_id"));
                genre.setName(resultSet.getString("genre_name"));
                Optional<HashSet<Genre>> genresFilm = Optional.ofNullable(filmsMap.get(film.getId()).getGenres());
                if (genresFilm.isEmpty()) {
                    filmsMap.get(film.getId()).setGenres(new HashSet<Genre>());
                    filmsMap.get(film.getId()).getGenres().add(genre);
                } else {
                    genresFilm.get().add(genre);
                }
            }
            if (resultSet.getInt("directors_id") > 0) {
                Director director = new Director();
                director.setId(resultSet.getInt("directors_id"));
                director.setName(resultSet.getString("directors_name"));
                Optional<Set<Director>> directorsFilm = Optional.ofNullable(filmsMap.get(film.getId()).getDirectors());
                if (directorsFilm.isEmpty()) {
                    filmsMap.get(film.getId()).setDirectors(new HashSet<Director>());
                    filmsMap.get(film.getId()).getDirectors().add(director);
                } else {
                    directorsFilm.get().add(director);
                }
            } else {
                Optional<Set<Director>> directorsFilm = Optional.ofNullable(filmsMap.get(film.getId()).getDirectors());
                if (directorsFilm.isEmpty()) {
                    filmsMap.get(film.getId()).setDirectors(new HashSet<Director>());
                }
            }
        } while (resultSet.next());
        List<Film> films = new ArrayList<>();
        films.addAll(filmsMap.values());
        return films;
    }
}
