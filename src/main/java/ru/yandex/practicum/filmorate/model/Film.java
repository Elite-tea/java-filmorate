package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private Integer id; // Id фильма
    @NotBlank
    private String name; // Название фильма
    @NotBlank
    @Size(max = 200)
    private String description; // Описание фильма
    private LocalDate releaseDate; // Дата релиза
    @PositiveOrZero
    private int duration; // Продолжительность фильма
}
