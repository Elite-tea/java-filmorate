package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс-модель для создания объекта лайка со свойствами <b>filmId<b/>, <b>userId<b/>.
 * Содержит в себе информацию об идентификаторе фильма и пользователя, который поставил лайк
 */
@Data
@NoArgsConstructor
public class Like {
    /**
     * Поле содержащие айди фильма
     */
    Long filmId;
    /**
     * Поле содержащие айди пользователя
     */
    Long userId;
}
