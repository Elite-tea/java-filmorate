package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс-модель для создания объекта оценки со свойствами <b>filmId<b/>, <b>userId<b/>, <b>rate</b>
 * Содержит в себе информацию об идентификаторе фильма и пользователя, который поставил оценку
 */
@Data
@NoArgsConstructor
public class Rate {
    /**
     * Поле содержащие идентификатор фильма
     */
    private Long filmId;
    /**
     * Поле содержащие идентификатор пользователя
     */
    private Long userId;
    /**
     * Поле содержащее оценку фильма от пользователя
     */
    private int rate;
}
