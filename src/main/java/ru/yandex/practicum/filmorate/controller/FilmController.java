package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SortBy;
import ru.yandex.practicum.filmorate.service.FilmDbService;

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
    private final FilmDbService filmService;

    /**
     * Добавляет фильм в хранилище.
     *
     * @param film объект фильма.
     */
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    /**
     * Обновляет фильм в хранилище.
     *
     * @param film объект фильма.
     */
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
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
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLike(userId, id);
    }

    /**
     * Запрос фильмов
     *
     * @return возвращает коллекцию фильмов
     */
    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getFilms();
    }

    /**
     * Запрос фильма по id
     *
     * @param id id фильма
     * @return возвращает фильм
     */
    @GetMapping("{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmService.getFilmById(id);
    }

    /**
     * Запрос фильмов по количеству лайков
     *
     * @param count количество попавших в топ фильмов(Если не указано, то 10)
     * @return возвращает список фильмов с количеством лайков (От большего к меньшему)
     */
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@PathVariable @RequestParam(defaultValue = "10") Integer count) {
        return filmService.getPopularFilms(count);
    }

    /**
     * Метод возвращает список отсортированных фильмов по году, либо по количеству лайков
     *
     * @param directorId идентификатор режиссера, по которому необходима сортировка
     * @param sortBy enum вида сортировки LIKE, либо YEAR
     * @return возвращает список, в зависимости от необходимого параметра сортировки
     */
    @GetMapping("/director/{directorId}")
    public List<Film> getSortedFilms(@PathVariable Integer directorId, @RequestParam String sortBy) {
        return filmService.getDirectorsFilms(directorId, SortBy.valueOf(sortBy.toUpperCase()));
    }
}
