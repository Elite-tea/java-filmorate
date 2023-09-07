package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.DirectorDbService;
import ru.yandex.practicum.filmorate.service.FilmDbService;

import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmControllerTest {
    private final FilmDbService filmService;
    private final DirectorDbService directorService;
    private final JdbcTemplate jdbcTemplate;

    private final Film film = new Film("Ron's Gone Wrong", "The cartoon about a funny robot",
            LocalDate.of(2021, 10, 22), 107);
    private final Director director = new Director("Octavio E. Rodriguez");

    @AfterEach
    void afterEach() {
        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("DELETE FROM film");
        jdbcTemplate.execute("DELETE FROM directors");
    }

    @Test
    void addFilm_shouldAddFilm() {
        film.setMpa(new Mpa(1));
        Film theFilm = filmService.addFilm(film);

        Assertions.assertEquals("Ron's Gone Wrong", theFilm.getName());
    }

    @Test
    void addFilm_shouldAddFilmWithDirector() {
        Director theDirector = directorService.addDirector(director);
        film.setMpa(new Mpa(1));
        film.setDirectors(Set.of(new Director(theDirector.getId())));
        Film theFilm = filmService.addFilm(film);

        Assertions.assertEquals(film.getName(), theFilm.getName());
        Assertions.assertEquals(Set.of(theDirector), theFilm.getDirectors());
    }

    @Test
    void updateFilm_shouldUpdateFilmWithDirector() {
        Director theDirector = directorService.addDirector(director);
        film.setMpa(new Mpa(1));
        Film theFilm = filmService.addFilm(film);
        theFilm.setDirectors(Set.of(theDirector));
        Film updatedFilm = filmService.updateFilm(theFilm);
        Set<Director> setOfDirectors = Set.of(new Director(theDirector.getId(), "Octavio E. Rodriguez"));

        Assertions.assertEquals(updatedFilm.getDirectors(), setOfDirectors);
    }

    @Test
    void updateFilm_shouldUpdateFilmAndDeleteDirector() {
        Director theDirector = directorService.addDirector(director);
        film.setMpa(new Mpa(1));
        film.setDirectors(Set.of(theDirector));
        Film theFilm = filmService.addFilm(film);
        theFilm.setDirectors(null);
        Film updatedFilm = filmService.updateFilm(theFilm);

        Assertions.assertEquals(0, updatedFilm.getDirectors().size());
    }

}
