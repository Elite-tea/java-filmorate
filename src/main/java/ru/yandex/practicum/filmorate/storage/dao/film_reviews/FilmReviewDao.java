package ru.yandex.practicum.filmorate.storage.dao.film_reviews;

/**
 * Интерфейс для работы с логикой касающиеся отзывов реализован в {@link FilmReviewDaoImpl}.
 */
public interface FilmReviewDao {
    
    /**
     * Добавление лайка отзыву
     *
     * @param reviewId отзыв, которому добавляют лайк.
     * @param userId пользователь, добавляющий лайк.
     */
    void addLikeToReview(Integer reviewId, Long userId);

    /**
     * Удаление лайка у отзыва
     *
     * @param reviewId отзыв, которому удаляют лайк.
     * @param userId пользователь, удаляющий лайк.
     */
    void deleteLikeFromReview(Integer reviewId, Long userId);

    /**
     * Добавление дизлайка отзыву
     *
     * @param reviewId отзыв, которому добавляют дизлайк.
     * @param userId пользователь, добавляющий дизлайк.
     */
    void addDislikeToReview(Integer reviewId, Long userId);

    /**
     * Удаление дизлайка отзыву
     *
     * @param reviewId отзыв, которому удаляют дизлайк.
     * @param userId пользователь,удаляющий дизлайк.
     */
    void deleteDislikeFromReview(Integer reviewId, Long userId);
}
