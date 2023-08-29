package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Класс-модель для создания объекта жанра со свойствами <b>id<b/>, <b>name<b/>.
 */
@Data
@NoArgsConstructor
public class Genre {
    /**
     * Поле идентификатор жанра
     */
    @NotNull Integer id;
    /**
     * Поле содержащие имя жанра
     */
    @NotNull String name;
}
