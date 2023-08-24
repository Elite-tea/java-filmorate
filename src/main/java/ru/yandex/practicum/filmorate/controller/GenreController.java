package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreDbService;

import java.util.Collection;

/**
 * Класс-контроллер для получения данных о жанрах, а так же реализации API со свойством <b>genreService</b>.
 */
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreDbService genreService;

    /**
     * Запрос жанра по id
     *
     * @param id айди запрашиваемого жанра
     * @return возвращает объект жанра
     */
    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Integer id) {
        return genreService.getGenreById(id);
    }

    /**
     * Запрос всех жанров
     *
     * @return возвращает коллекцию жанров
     */
    @GetMapping
    public Collection<Genre> getGenres() {
        return genreService.getGenres();
    }
}
