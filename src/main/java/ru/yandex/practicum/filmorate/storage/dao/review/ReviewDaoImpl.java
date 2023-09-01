package ru.yandex.practicum.filmorate.storage.dao.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.mapper.ReviewMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewDaoImpl implements ReviewDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Review addReview(Review review) {
        log.debug("addReview({})", review);
        jdbcTemplate.update(
                "INSERT INTO reviews (content, is_positive, user_id, film_id, useful) VALUES (?,?,?,?,?)",
                review.getContent(), review.getIsPositive(), review.getUserId(), review.getFilmId(), review.getUseful());
        Review thisReview = jdbcTemplate.queryForObject(
                "SELECT review_id, content, is_positive, user_id, film_id, useful " +
                        "FROM reviews WHERE content=? AND is_positive=? AND user_id=? AND film_id=? AND useful=?",
                new ReviewMapper(), review.getContent(), review.getIsPositive(), review.getUserId(), review.getFilmId(),
                review.getUseful());
        log.trace("Добавлен отзыв: {}", thisReview);
        return thisReview;
    }

    @Override
    public Review updateReview(Review review) {
        log.debug("updateReview({})", review);
        jdbcTemplate.update("UPDATE reviews SET useful=?, is_positive=?, content=? WHERE review_id=?",
                review.getUseful(), review.getIsPositive(), review.getContent(), review.getReviewId());
        Review thisReview = getReviewById(review.getReviewId());
        log.trace("Обновлён отзыв: {}", thisReview);
        return thisReview;
    }

    @Override
    public void deleteReviewById(Integer id) {
        log.debug("deleteReviewById({})", id);
        jdbcTemplate.update("DELETE FROM reviews WHERE review_id=?", id);
        log.trace("Удалён отзыв с идентификатором {}", id);
    }

    @Override
    public Review getReviewById(Integer id) {
        log.debug("getReviewById({})", id);
        Review review = jdbcTemplate.queryForObject(
                "SELECT review_id, content, is_positive, user_id, film_id, useful " +
                "FROM reviews WHERE review_id=?", new ReviewMapper(), id);
        log.trace("Возвращён отзыв с идентификатором {}: {}", id, review);
        return review;
    }

    @Override
    public List<Review> getReviews(Long id, int count) {
        log.debug("getReviews({}, {})", id, count);
        List<Review> list;
        if (id == -1) {
            list = new ArrayList<>(jdbcTemplate.query(
                    "SELECT review_id, content, is_positive, user_id, film_id, useful FROM reviews " +
                            "GROUP BY review_id ORDER BY useful DESC LIMIT ?", new ReviewMapper(), count));
            log.trace("Возвращён список всех отзывов: {}", list);
        } else {
            list = new ArrayList<>(jdbcTemplate.query(
                    "SELECT review_id, content, is_positive, user_id, film_id, useful FROM reviews " +
                            "WHERE film_id=? GROUP BY review_id ORDER BY useful DESC LIMIT ?",
                    new ReviewMapper(), id, count));
            log.trace("Возвращён список отзывов по идентификатору фильма {}: {}", id, list);
        }
        return list;
    }

    @Override
    public boolean isContains(Integer id) {
        log.debug("isContains({})", id);
        try {
            getReviewById(id);
            log.trace("Информация по отзыву с идентификатором {} найдена", id);
            return true;
        } catch (EmptyResultDataAccessException exception) {
            log.trace("Нет информации по отзыву с идентификатором {}", id);
            return false;
        }
    }

    @Override
    public void increaseScore(Review review) {
        log.debug("increaseScore({})", review);
        review.setUseful(review.getUseful() + 1);
            jdbcTemplate.update("UPDATE reviews SET content=?, is_positive=?, user_id=?, film_id=?, useful=? " +
                            "WHERE review_id=?", review.getContent(), review.getIsPositive(), review.getUserId(),
                    review.getFilmId(), review.getUseful(), review.getReviewId());
        log.trace("У отзыва {} увеличился уровень полезности на единицу", review);
    }

    @Override
    public void decreaseScore(Review review) {
        log.debug("decreaseScore({})", review);
        review.setUseful(review.getUseful() - 1);
            jdbcTemplate.update("UPDATE reviews SET content=?, is_positive=?, user_id=?, film_id=?, useful=? " +
                            "WHERE review_id=?", review.getContent(), review.getIsPositive(), review.getUserId(),
                    review.getFilmId(), review.getUseful(), review.getReviewId());
        log.trace("У отзыва {} уменьшился уровень полезности на единицу", review);
    }
}
