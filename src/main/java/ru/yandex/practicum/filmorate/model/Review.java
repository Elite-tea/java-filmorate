package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * Класс-модель для создания объекта отзыва со свойствами <b>id<b/>, <b>content<b/>, <b>isPositive<b/>, <b>userId<b/>,
 * <b>filmId<b/>, <b>useful<b/>.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    /**
     * Поле с идентификатором отзыва.
     */
    private Integer reviewId;
    /**
     * Поле с содержанием комментария.
     */
    @NotNull
    @NotBlank
    private String content;
    /**
     * Поле с содержанием типа отзыва, где true - полезный, false - бесполезный.
     */
    private Boolean isPositive;
    /**
     * Поле с идентификатором пользователя, оставившего комментарий.
     */
    @NotNull
    private Long userId;
    /**
     * Поле с идентификатором фильма, которому оставили комментарий.
     */
    @NotNull
    private Long filmId;
    /**
     * Поле с рейтингом полезности комментария, если тип отзыва true - рейтинг увеличивается на единицу.
     */
    @PositiveOrZero
    private Integer useful;

    /**
     * Конструктор создание нового объекта отзыва.
     *
     * @see Review#Review(String, Boolean, Long, Long)
     */
    @Autowired
    public Review(String content, Boolean isPositive, Long userId, Long filmId) {
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
    }
}
