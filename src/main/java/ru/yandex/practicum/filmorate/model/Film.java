package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Класс-модель для создания фильма со свойствами <b>id<b/>, <b>name<b/>, <b>description<b/>, <b>releaseDate<b/>, <b>duration<b/>, <b>like<b/>.
 */
@Data
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
     * Поле с перечислением пользователей поставивших лайки
     */
    private Set<Long> like = new HashSet<>();
    /**
     * Поле с перечислением жанров фильма
     */
    private LinkedHashSet<String> genre;
    /**
     * Поле с указанием рейтинга фильма
     */
    private String mpa;

    /**
     * Конструктор создание нового объекта фильма.
     *
     * @see Film#Film(Long, String, String, LocalDate, int)
     */
    @Autowired
    public Film(Long id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }


    /**
     * Метод добавления лайка
     *
     * @param id id пользователя поставившего свой лайк
     */
    public void addLike(Long id) {
        like.add(id);
    }

    /**
     * Метод удаления лайка у фильма
     *
     * @param id id пользователя удалившего свой лайк
     */
    public void deleteLike(Long id) {
        like.remove(id);
    }

    /**
     * Метод получения значения количества лайков у фильма
     *
     * @return возвращает количество лайков
     */
    public Integer getLike() {
        return like.size();
    }
}
