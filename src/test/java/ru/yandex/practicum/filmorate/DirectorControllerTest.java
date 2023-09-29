package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.DirectorDbService;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.storage.dao.director.DirectorDao;

import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DirectorControllerTest {
    private final DirectorDbService directorService;
    private final DirectorDao directorDao;
    private final FilmDbService filmService;
    private final JdbcTemplate jdbcTemplate;

    private final Film film = new Film("Ron's Gone Wrong", "The cartoon about a funny robot",
            LocalDate.of(2021, 10, 22), 107);
    private final Genre genre = new Genre(1);
    private final Director director = new Director("Miyazaki Hayao");

    @AfterEach
    void afterEach() {
        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("DELETE FROM film");
        jdbcTemplate.execute("DELETE FROM directors");
    }

    @Test
    void addDirector_shouldAddDirector() {
        Director theDirector = directorService.addDirector(director);

        Assertions.assertEquals(director.getName(), theDirector.getName());
    }

    @Test
    void updateDirectorData_shouldNotUpdateDirectorIfIdIncorrect() {
        Assertions.assertThrows(NotFoundException.class, () -> directorService.updateDirector(director));
    }

    @Test
    void getDirectorsByFilmId_shouldReturnListOfDirectors() {
        Director theDirector = directorService.addDirector(director);
        film.setMpa(new Mpa(1));
        film.setDirectors(Set.of(new Director(theDirector.getId())));
        Film theFilm = filmService.addFilm(film);
        Set<Director> directors = Set.of(theDirector);

        Assertions.assertEquals(directors, directorDao.getDirectorsByFilm(theFilm.getId()));
    }

    @Test
    void getDirectorById_shouldReturnDirectorWithId1() {
        Director thisDirector = directorService.addDirector(director);

        Assertions.assertEquals(1, thisDirector.getId());
    }

    @Test
    void getDirectors_shouldReturnListOfDirectorsWithSize3() {
        Director thisDirector = directorService.addDirector(director);
        Director oneMoreDirector = directorService.addDirector(new Director("Steven Spielberg"));
        Director otherDirector = directorService.addDirector(new Director("Tim Burton"));
        Set<Director> setOfDirectors = Set.of(thisDirector, oneMoreDirector, otherDirector);

        Assertions.assertEquals(directorService.getDirectors().size(), setOfDirectors.size());
    }
}
