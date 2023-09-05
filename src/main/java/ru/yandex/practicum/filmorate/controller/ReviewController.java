package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewDbService;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс-контроллер для CRUD операций с отзывами и реализации API со свойством <b>ReviewDbService</b>
 */
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    /**
     * Поле сервис
     */
    private final ReviewDbService reviewService;

    /**
     * Добавляет отзыв в хранилище
     *
     * @param review объект отзыва
     * @return возвращает объект отзыва
     */
    @PostMapping
    public Review addReview(@Valid @RequestBody Review review) {
        return reviewService.addReview(review);
    }

    /**
     * Обновляет отзыв в хранилище
     *
     * @param review объект отзыва
     * @return возвращает объект отзыва
     */
    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    /**
     * Удаляет объект отзыва в хранилище
     *
     * @param id идентификатор объекта
     */
    @DeleteMapping("/{id}")
    public void deleteReviewById(@PathVariable Integer id) {
        reviewService.deleteReviewById(id);
    }

    /**
     * Получает объект отзыва по идентификатору
     *
     * @param id идентификатор объекта отзыва
     * @return возвращает запрошенный объект отзыва
     */
    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable Integer id) {
        return reviewService.getReviewById(id);
    }

    /**
     * Получение всех отзывов по идентификатору фильма, если фильм не указан то все. Если кол-во не указано, то 10
     *
     * @param filmId идентификатор фильма
     * @param count количество получаемых отзывов, по дефолту значение 10
     * @return возвращает список отзывов
     */
    @GetMapping
    public List<Review> getReviews(
            @RequestParam(name = "filmId", defaultValue = "-1", required = false) Long filmId,
            @RequestParam(name = "count", defaultValue = "10", required = false) int count
    ) {
        return reviewService.getReviews(filmId, count);
    }

    /**
     * Поставить лайк отзыву
     *
     * @param id идентификатор отзыва
     * @param userId идентификатор пользователя, который ставит лайк
     */
    @PutMapping("/{id}/like/{userId}")
    public void likeAReview(@PathVariable Integer id, @PathVariable Long userId) {
        reviewService.addLikeToReview(id, userId);
    }

    /**
     * Поставить дизлайк отзыву
     *
     * @param id идентификатор отзыва
     * @param userId идентификатор пользователя, который ставить дизлайк
     */
    @PutMapping("/{id}/dislike/{userId}")
    public void dislikeAReview(@PathVariable Integer id, @PathVariable Long userId) {
        reviewService.addDislikeToReview(id, userId);
    }

    /**
     * Удаление лайка у отзыва
     *
     * @param id идентификатор отзыва
     * @param userId идентификатор пользователя, который удаляет лайк
     */
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeOfReview(@PathVariable Integer id, @PathVariable Long userId) {
        reviewService.deleteLikeFromReview(id, userId);
    }

    /**
     * Удаление дизлайка у отзыва
     *
     * @param id идентификатор отзыва
     * @param userId идентификатор пользователя, который удаляет дизлайк
     */
    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislikeOfReview(@PathVariable Integer id, @PathVariable Long userId) {
        reviewService.deleteDislikeFromReview(id, userId);
    }
}
