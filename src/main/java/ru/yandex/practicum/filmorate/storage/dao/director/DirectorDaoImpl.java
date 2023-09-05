package ru.yandex.practicum.filmorate.storage.dao.director;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.mapper.DirectorMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DirectorDaoImpl implements DirectorDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Director addDirector(Director director) {
        log.debug("addDirector({})", director);
        jdbcTemplate.update("INSERT INTO directors (director_name) VALUES (?)", director.getName());
        Director thisDirector = jdbcTemplate.queryForObject("SELECT director_id, director_name FROM directors " +
                "WHERE director_name=?", new DirectorMapper(), director.getName());
        log.trace("Добавлены данные о новом режиссере: {}", thisDirector);
        return thisDirector;
    }

    @Override
    public void addDirectorsToFilm(Long filmId, Set<Director> directors) {
        log.debug("addDirectorsToFilm({}, {})", filmId, directors);
        for (Director director : directors) {
            jdbcTemplate.update("INSERT INTO film_directors (film_id, director_id) VALUES (?,?)",
                    filmId, director.getId());
        }
        log.trace("К фильму с идентификатором {} были добавлены следующие режиссеры: {}", filmId, directors);
    }

    @Override
    public Director updateDirectorData(Director director) {
        log.debug("updateDirectorData({})", director);
        jdbcTemplate.update("UPDATE directors SET director_name=? WHERE director_id=?",
                director.getName(), director.getId());
        Director thisDirector = jdbcTemplate.queryForObject("SELECT director_id, director_name FROM directors " +
                "WHERE director_name=?", new DirectorMapper(), director.getName());
        log.trace("Обновлены данные о режиссере: {}", thisDirector);
        return thisDirector;
    }

    @Override
    public void updateDirectorsInFilm(Long filmId, Set<Director> directors) {
        log.debug("updateDirectorsInFilm({}, {})", filmId, directors);
        deleteDirectorsFromFilm(filmId);
        addDirectorsToFilm(filmId, directors);
        log.trace("У фильма {} обновлен список режиссеров: {}", filmId, directors);
    }

    @Override
    public Director getDirectorById(Integer id) {
        log.debug("getDirectorById({})", id);
        Director director = jdbcTemplate.queryForObject("SELECT director_id, director_name FROM directors " +
                "WHERE director_id=?", new DirectorMapper(), id);
        log.trace("Возвращен режиссер с идентификатором {}: {}", id, director);
        return director;
    }

    @Override
    public Set<Director> getDirectorsByFilm(Long filmId) {
        log.debug("getDirectorsByFilm({})", filmId);
        Set<Director> directors = new HashSet<>(jdbcTemplate.query("SELECT f.director_id, d.director_name " +
                "FROM film_directors AS f INNER JOIN directors AS d ON f.director_id = d.director_id WHERE " +
                "f.film_id=? ORDER BY d.director_id", new DirectorMapper(), filmId));
        log.trace("Возвращен список режиссеров фильма {}: {}", filmId, directors);
        return directors;
    }

    @Override
    public void deleteDirectorById(Integer id) {
        log.debug("deleteDirectorById({})", id);
        jdbcTemplate.update("DELETE FROM directors WHERE director_id=?", id);
        log.trace("Удален режиссер с идентификатором {}", id);
    }

    @Override
    public void deleteDirectorsFromFilm(Long filmId) {
        log.debug("deleteDirectorsFromFilm({})", filmId);
        jdbcTemplate.update("DELETE FROM film_directors WHERE film_id=?", filmId);
        log.trace("У фильма {} были удалены режиссеры", filmId);
    }

    @Override
    public List<Director> getDirectors() {
        log.debug("getDirectors()");
        List<Director> listOfDirectors = new ArrayList<>(jdbcTemplate.query("SELECT * FROM directors",
                new DirectorMapper()));
        log.trace("Возвращён список режиссеров: {}", listOfDirectors);
        return listOfDirectors;
    }

    @Override
    public boolean isContains(Integer id) {
        log.debug("isContains({})", id);
        try {
            getDirectorById(id);
            log.trace("Режиссер с идентификатором {} найден", id);
            return true;
        } catch (EmptyResultDataAccessException exception) {
            log.trace("Нет информации о режиссере с идентификатором {}", id);
            return false;
        }
    }
}
