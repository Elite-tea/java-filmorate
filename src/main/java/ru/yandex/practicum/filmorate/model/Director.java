package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс-модель для создания режиссера со свойствами <b>id<b/>, <b>name<b/>.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Director {
    /**
     * Поле идентификатор режиссера
     */
    private Integer id;
    /**
     * Поле имени и фамилии режиссера
     */
    private String name;

    public Director(String name) {
        this.name = name;
    }

    public Director(Integer id) {
        this.id = id;
    }
}
