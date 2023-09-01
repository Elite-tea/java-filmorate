package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.ReviewDbService;
import ru.yandex.practicum.filmorate.storage.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;

/**
 * Класс-тестирование для ReviewController.class
 */
@SpringBootTest
@AutoConfigureTestDatabase
public class ReviewControllerTest {
    private final ReviewDbService reviewService;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewControllerTest(ReviewDbService reviewService,
                                @Qualifier("UserDbStorage") UserStorage userStorage,
                                @Qualifier("FilmDbStorage") FilmStorage filmStorage,
                                JdbcTemplate jdbcTemplate) {
        this.reviewService = reviewService;
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    private final User user = new User("user@ya.ru", "flying_dragon", "Andrew",
            LocalDate.of(1996, 12, 3));
    private final Film film = new Film("Ron's Gone Wrong", "The cartoon about a funny robot",
            LocalDate.of(2021, 10, 22), 107);
    private final Genre genre = new Genre(1);

    @AfterEach
    void afterEach() {
        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("DELETE FROM film");
        jdbcTemplate.execute("DELETE FROM reviews");
    }

    @Test
    void addReview_shouldAddReview() {
        User thisUser = userStorage.create(user);
        film.setMpa(new Mpa(1));
        HashSet<Genre> genres = new HashSet<>();
        genres.add(genre);
        film.setGenres(genres);
        Film thisFilm = filmStorage.addFilm(film);
        Review thisReview = reviewService.addReview(
                new Review("Very bad movie", false, thisUser.getId(), thisFilm.getId()));

        Assertions.assertEquals(thisReview, reviewService.getReviewById(thisReview.getReviewId()));
    }

    @Test
    void addReview_shouldNotAddFailFilmReview() {
        User thisUser = userStorage.create(user);
        film.setMpa(new Mpa(1));
        HashSet<Genre> genres = new HashSet<>();
        genres.add(genre);
        film.setGenres(genres);
        Film thisFilm = filmStorage.addFilm(film);
        Review thisReview = reviewService.addReview(
                new Review("Very bad movie", false, thisUser.getId(), thisFilm.getId()));
        thisReview.setFilmId(-1L);

        Assertions.assertThrows(NotFoundException.class, () -> reviewService.addReview(thisReview));
    }

    @Test
    void updateReview_shouldUpdateReview() {
        User thisUser = userStorage.create(user);
        film.setMpa(new Mpa(1));
        HashSet<Genre> genres = new HashSet<>();
        genres.add(genre);
        film.setGenres(genres);
        Film thisFilm = filmStorage.addFilm(film);
        Review thisReview = reviewService.addReview(
                new Review("Very bad movie", false, thisUser.getId(), thisFilm.getId()));
        thisReview.setIsPositive(true);
        Review updatedReview = reviewService.updateReview(thisReview);

        Assertions.assertEquals(thisReview.getReviewId(), updatedReview.getReviewId());
    }

    @Test
    void likeAReview_shouldLikeByUser() {
        User thisUser = userStorage.create(user);
        film.setMpa(new Mpa(1));
        HashSet<Genre> genres = new HashSet<>();
        genres.add(genre);
        film.setGenres(genres);
        Film thisFilm = filmStorage.addFilm(film);
        Review thisReview = reviewService.addReview(
                new Review("Very bad movie", false, thisUser.getId(), thisFilm.getId()));
        reviewService.addLikeToReview(thisReview.getReviewId(), thisUser.getId());
        Review updatedReview = reviewService.getReviewById(thisReview.getReviewId());

        Assertions.assertEquals(1, updatedReview.getUseful());
    }

    @Test
    void getReviews_shouldReturnReviewListLength1() {
        User thisUser = userStorage.create(user);
        film.setMpa(new Mpa(1));
        HashSet<Genre> genres = new HashSet<>();
        genres.add(genre);
        film.setGenres(genres);
        Film thisFilm = filmStorage.addFilm(film);
        Review thisReview = reviewService.addReview(
                new Review("Very bad movie", false, thisUser.getId(), thisFilm.getId()));
        reviewService.addLikeToReview(thisReview.getReviewId(), thisUser.getId());
        Review updatedReview = reviewService.getReviewById(thisReview.getReviewId());

        Assertions.assertTrue(reviewService.getReviews(-1L, 10).contains(updatedReview));
    }

    @Test
    void getReviews_shouldReturnListOfReviewsForFilm1() {
        User thisUser = userStorage.create(user);
        film.setMpa(new Mpa(1));
        HashSet<Genre> genres = new HashSet<>();
        genres.add(genre);
        film.setGenres(genres);
        Film thisFilm = filmStorage.addFilm(film);
        Review thisReview = reviewService.addReview(
                new Review("Very bad movie", false, thisUser.getId(), thisFilm.getId()));

        Assertions.assertTrue(reviewService.getReviews(thisFilm.getId(), 1).contains(thisReview));
    }

}
