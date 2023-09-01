package ru.yandex.practicum.filmorate.storage.dao.review;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

/**
 * Интерфейс для работы с логикой касающиеся отзывов реализован в {@link ReviewDaoImpl}.
 */
public interface ReviewDao {
    /**
     * Добавление отзыва фильму.
     *
     * @param review добавляемый отзыв.
     * @return review возвращаемый отзыв с идентификатором, присвоенным в БД.
     */
    Review addReview(Review review);

    /**
     * Обновление отзыва фильму.
     *
     * @param review обновляемый отзыв.
     * @return review возвращаемый отзыв с идентификатором, присвоенным в БД.
     */
    Review updateReview(Review review);

    /**
     * Удаление отзыва по идентификатору.
     *
     * @param id идентификатор отзыва, добавленного в БД.
     * @throws NotFoundException генерирует исключение, если отзыв с переданным идентификатором не найден.
     */
    void deleteReviewById(Integer id);

    /**
     * Получение отзыва по идентификатору.
     *
     * @param id идентификатор отзыва.
     * @return review возвращаемый отзыв.
     * @throws NotFoundException генерирует исключение, если отзыв с переданным идентификатором не найден.
     */
    Review getReviewById(Integer id);

    /**
     * Получение всех отзывов по идентификатору фильма, если фильм не указан то все. Если кол-во не указано, то 10.
     *
     * @param id идентификатор отзыва.
     * @return List возвращается лист с отзывами фильма.
     */
    List<Review> getReviews(Long id, int count);

    /**
     * Проверка на наличие отзыва в БД по идентификатору.
     *
     * @param id идентификатор отзыва.
     * @return возвращает true - если отзыв найден, false - если не найден
     * @catch отлавливает EmptyResultDataAccessException в случае, если в БД нет информации по отзыву.
     */
    boolean isContains(Integer id);

    /**
     * Метод увеличивающий полезность отзыва.
     *
     * @param review передаваемый отзыв.
     */
    void increaseScore(Review review);

    /**
     * Метод уменьшающий полезность отзыва.
     *
     * @param review передаваемый отзыв.
     */
    void decreaseScore(Review review);
}