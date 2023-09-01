package ru.yandex.practicum.filmorate.storage.dao.like;


import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@AllArgsConstructor
@Component
public class LikeDaoImpl implements LikeDao {
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
    public int checkLikes(Long filmId) {
        return Objects.requireNonNull(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM likes WHERE film_id=?",
                                      Integer.class, filmId));
    }
}
