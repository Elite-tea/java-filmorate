package ru.yandex.practicum.filmorate.storage.dao;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Component
public class LikeDaoImpl implements LikeDao {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Long userId, Long filmId) {

        jdbcTemplate.update("INSERT INTO likes (user_id, film_id) VALUES (?,?)", userId, filmId);
    }

    @Override
    public void deleteLike(Long userId, Long filmId) {
        jdbcTemplate.update("DELETE FROM likes WHERE user_id = ? AND film_id = ?", userId, filmId);
    }

    @Override
    public List<Film> getPopularFilm(int topNumber) {
        return jdbcTemplate.query("SELECT * FROM film LEFT JOIN likes ON film.film_id = like.film_id " +
                                     "GROUP BY film.film_id ORDER BY COUNT(like.user_id) DESC LIMIT = ?",
                                      new FilmMapper(), topNumber);
    }

    @Override
    public int examinationLikes(Long filmId) {
        return Objects.requireNonNull(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM likes WHERE film_id=?",
                                      Integer.class, filmId));
    }
}
