package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * Класс-контроллер для оценки фильмов и реализации API со свойством <b>filmService</b>.
 */

@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    /**
     * Поле сервис
     */
    @Autowired
    private final FilmService filmService;


    /**
     * Добавляет фильм в хранилище.
     *
     * @param film объект фильма.
     */
    @PostMapping
    public Film addFilms(@Valid @RequestBody Film film) {
        return filmService.addFilms(film);
    }

    /**
     * Обновляет фильм в хранилище.
     *
     * @param film объект фильма.
     */
    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        return filmService.put(film);
    }

    /**
     * Добавляет лайк фильму
     *
     * @param id     id фильма.
     * @param userId id поставившего лайк.
     */
    @PutMapping("{id}/like/{userId}")
    public void likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(userId, id);
    }

    /**
     * Удаляет лайк у фильма
     *
     * @param id     id фильма.
     * @param userId id удалившего свой лайк.
     */
    @DeleteMapping("{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLike(userId, id);
    }

    /**
     * Запрос фильмов
     *
     * @return возвращает коллекцию фильмов
     */
    @GetMapping
    public Collection<Film> getFilm() {
        return filmService.getFilm();
    }

    /**
     * Запрос фильма по id
     *
     * @param id id фильма
     * @return возвращает фильм
     */
    @GetMapping("{id}")
    public Film getByIdFilm(@PathVariable Long id) {
        return filmService.getByIdFilm(id);
    }

    /**
     * Запрос фильмов по количеству лайков
     *
     * @param count количество попавших в топ фильмов(Если не указано, то 10)
     * @return возвращает список фильмов с количеством лайков (От большего к меньшему)
     */
    @GetMapping("popular")
    public List<Film> getPopularFilm(@PathVariable @RequestParam(defaultValue = "10") Integer count) {
        return filmService.getPopularFilm(count);
    }
}
