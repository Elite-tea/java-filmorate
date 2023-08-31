package ru.yandex.practicum.filmorate.storage.dao.film_reviews;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.mapper.FilmReviewMapper;

@Slf4j
@RequiredArgsConstructor
@Component
public class FilmReviewDaoImpl implements FilmReviewDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLikeToReview(Integer reviewId, Long userId) {
        jdbcTemplate.update("MERGE INTO film_reviews (review_id, user_id, is_positive) VALUES (?, ?, ?)",
                reviewId, userId, Boolean.TRUE);
        updateUsefulnessScore(reviewId);
    }

    @Override
    public void deleteLikeFromReview(Integer reviewId, Long userId) {
        jdbcTemplate.update("DELETE FROM film_reviews WHERE review_id=? AND user_id=?");
        updateUsefulnessScore(reviewId);
    }

    @Override
    public void addDislikeToReview(Integer reviewId, Long userId) {
        jdbcTemplate.update("MERGE INTO film_reviews (review_id, user_id, is_positive) VALUES (?, ?, ?)",
                reviewId, userId, Boolean.FALSE);
        updateUsefulnessScore(reviewId);
    }

    @Override
    public void deleteDislikeFromReview(Integer reviewId, Long userId) {
        jdbcTemplate.update("DELETE FROM film_reviews WHERE review_id=? AND user_id=?");
        updateUsefulnessScore(reviewId);
    }

    private void updateUsefulnessScore(Integer reviewId) {
        int usefulnessScore = getUsefulnessScore(reviewId);
        jdbcTemplate.update("UPDATE reviews SET useful=? WHERE review_id=?", usefulnessScore, reviewId);
    }

    private int getUsefulnessScore(Integer reviewId) {
        return jdbcTemplate.query("SELECT SUM(CASE WHEN is_positive = TRUE THEN 1 ELSE -1 END) useful " +
                "FROM film_reviews WHERE review_id =?", new FilmReviewMapper(), reviewId)
                .stream().findAny().orElse(0);
    }
}
