package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {

    @Autowired
    private final FilmStorage filmStorage;
    @Autowired
    private final FilmService filmService;

    @PostMapping
    public Film addFilms(@Valid @RequestBody Film film) {
        return filmStorage.addFilms(film);
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        return filmStorage.put(film);
    }

    @PutMapping("{id}/like/{userId}")
    public void likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(userId, id);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLike(userId, id);
    }

    @GetMapping
    public Collection<Film> getFilm() {
        return filmStorage.getFilm();
    }

    @GetMapping("{id}")
    public Film getByIdFilm(@PathVariable Long id) {
        return filmStorage.getByIdFilm(id);
    }

    @GetMapping("popular")
    public List<Film> getPopularFilm(@PathVariable @RequestParam(defaultValue = "10") Integer count) {
        return filmService.getPopularFilm(count);
    }
}
