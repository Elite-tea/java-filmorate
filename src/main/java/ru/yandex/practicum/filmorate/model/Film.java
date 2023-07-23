package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Long id; // Id фильма
    @NotBlank
    private String name; // Название фильма
    @NotBlank
    @Size(max = 200)
    private String description; // Описание фильма
    private LocalDate releaseDate; // Дата релиза
    @PositiveOrZero
    private int duration; // Продолжительность фильма
    private Set<Long> like = new HashSet<>(); //Список пользователей поставивших лайки

    @Autowired
    public Film(Long id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void addLike(Long id) {
        like.add(id);
    }

    public void deleteLike(Long id) {
        like.remove(id);
    }

    public Integer getLike() {
        return like.size();
    }
}
