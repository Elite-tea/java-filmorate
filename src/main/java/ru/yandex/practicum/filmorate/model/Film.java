package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс-модель для создания фильма со свойствами <b>id<b/>, <b>name<b/>, <b>description<b/>, <b>releaseDate<b/>,
 * <b>duration<b/>, <b>like<b/>.
 */
@Data
@NoArgsConstructor
public class Film {
    /**
     * Поле идентификатор фильма
     */
    private Long id;
    /**
     * Поле название фильма
     */
    @NotBlank
    private String name;
    /**
     * Поле описание фильма
     */
    @NotBlank
    @Size(max = 200)
    private String description;
    /**
     * Поле дата релиза
     */
    private LocalDate releaseDate;
    /**
     * Поле продолжительность фильма
     */
    @PositiveOrZero
    private int duration;
    /**
     * Поле с оценкой фильма
     */
    @PositiveOrZero
    @Min(0)
    @Max(10)
    private Double rate;
    /**
     * Поле с перечислением жанров фильма
     */
    private HashSet<Genre> genres;
    /**
     * Поле с указанием рейтинга фильма
     */
    @NotNull
    private Mpa mpa;
    /**
     * Поле с указанием режиссера фильма
     */
    private Set<Director> directors;

    /**
     * Конструктор создание нового объекта фильма.
     *
     * @see Film#Film(String, String, LocalDate, int)
     */
    @Autowired
    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}