package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс-модель для создания объекта друзей со свойствами <b>userId<b/>, <b>friendId<b/>, <b>status<b/>.
 */
@Data
@NoArgsConstructor
public class Friend {
    /**
     * Поле идентификатор пользователя
     */
    Long userId;
    /**
     * Поле идентификатор друга пользователя
     */
    Long friendId;
    /**
     * Поле статуса дружбы(true - принятая дружба, false - есть запрос на дружбу)
     */
    boolean status;
}