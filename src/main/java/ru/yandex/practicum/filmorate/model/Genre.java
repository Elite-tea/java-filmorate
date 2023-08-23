package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class Genre {
    @NotNull Integer id;
    @NotNull String name;

    public Genre(Integer id) {
        this.id = id;
    }
}
