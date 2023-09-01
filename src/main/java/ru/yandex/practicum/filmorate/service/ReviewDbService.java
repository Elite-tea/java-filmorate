package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.dao.film_reviews.FilmReviewDao;
import ru.yandex.practicum.filmorate.storage.dao.review.ReviewDao;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.util.List;

/**
 * Класс-сервис с логикой для оперирования отзывами
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewDbService {
    /**
     * Поле для доступа к операциям отзывов.
     */
    private final ReviewDao reviewDao;
    /**
     * Поле для доступа к операциям пользователей.
     */
    private final UserDbService userService;
    /**
     * Поле для доступа к операциям фильмов.
     */
    private final FilmDbService filmService;
    /**
     * Поле для доступа к операциям лайков и дизлайков для отзывов.
     */
    private final FilmReviewDao filmReview;

    public Review addReview(Review review) {
        checker(review.getFilmId(), review.getUserId());
        Validation.validationReview(review);
        return reviewDao.addReview(review);
    }

    public Review updateReview(Review review) {
        checker(review.getFilmId(), review.getUserId());
        Validation.validationReview(review);
        return reviewDao.updateReview(review);
    }

    public void deleteReviewById(Integer id) {
        if (!reviewDao.isContains(id) || id == null) {
            throw new NotFoundException("Отзыв не найден: пустой или неправильный идентификатор");
        }
        reviewDao.deleteReviewById(id);
    }

    public Review getReviewById(Integer id) {
        if (!reviewDao.isContains(id) || id == null) {
            throw new NotFoundException("Отзыв не найден: пустой или неправильный идентификатор");
        }
        return reviewDao.getReviewById(id);
    }

    public List<Review> getReviews(Long id, int count) {
        return reviewDao.getReviews(id, count);
    }

    public void addLikeToReview(Integer reviewId, Long userId) {
        filmReview.addLikeToReview(reviewId, userId);
    }

    public void addDislikeToReview(Integer reviewId, Long userId) {
        filmReview.addDislikeToReview(reviewId, userId);
    }

    public void deleteLikeFromReview(Integer reviewId, Long userId) {
        filmReview.deleteLikeFromReview(reviewId, userId);
    }

    public void deleteDislikeFromReview(Integer reviewId, Long userId) {
        filmReview.deleteDislikeFromReview(reviewId, userId);
    }

    private void checker(Long filmId, Long userId) {
        if (filmId == null || filmService.getFilmById(filmId) == null) {
            throw new NotFoundException("Не найден фильм c идентификатором " + filmId);
        }
        if (userId == null || userService.getUserById(userId) == null) {
            throw new NotFoundException("Не найден пользователь с идентификатором " + userId);
        }
    }
}
