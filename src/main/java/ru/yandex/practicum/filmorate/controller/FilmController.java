package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.assistant.SortBy;
import ru.yandex.practicum.filmorate.service.FilmDbService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
     * Удаляет фильм по идентификатору
     *
     * @param id идентификатор удаляемого фильма
     */
    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable Long id) {
        filmService.getFilmStorage().deleteFilm(id);
    }

    /**
     * Метод добавляет оценку фильму
     *
     * @param id идентификатор фильма, которому ставят оценку
     * @param userId идентификатор пользователя, который ставит оценку
     * @param rate оценка фильму
     */
    @PostMapping("/{id}/rate/{userId}")
    public void rateFilm(@PathVariable Long id, @PathVariable Long userId,
                         @RequestParam(defaultValue = "0") @Positive Integer rate) {
        filmService.rateFilm(id, userId, rate);
    }

    /**     * Метод обновляет оценку фильма
     *
     * @param id идентификатор фильма, которому обновляют оценку
     * @param userId идентификатор пользователя, который обновляет оценку
     * @param rate оценка фильму
     */
    @PutMapping("/{id}/rate/{userId}")
    public void updateFilmRate(@PathVariable Long id, @PathVariable Long userId,
                               @RequestParam(defaultValue = "0") @Positive Integer rate) {
        filmService.updateFilmRate(id, userId, rate);
    }

    /**
     * Метод удаления оценки у фильма
     *
     * @param id идентификатор фильма, которому удаляют оценку
     * @param userId идентификатор пользователя, который удаляет оценку
     */
    @DeleteMapping("/{id}/rate/{userId}")
    public void deleteFilmRate(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteFilmRate(id, userId);
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
    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmService.getFilmById(id);
    }

    /**
     * Запрос фильмов по количеству лайков или по жанру, по году релиза фильма или жанру и году сразу.
     *
     * @param count количество попавших в топ фильмов(Если не указано, то 10)
     * @param genreId идентификатор жанра (не обязательный параметр)
     * @param year год релиза фильмов (не обязательный параметр)
     * @return возвращает список фильмов с количеством лайков (От большего к меньшему),
     * можно фильтровать по жанру и году или жанру и году сразу.
     */
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") @Positive Integer count,
                                      @RequestParam Optional<Integer> genreId,
                                      @RequestParam Optional<Integer> year) {
        return filmService.getPopularFilms(count, genreId, year);
    }

    /**
     * Вывод общих с другом фильмов с сортировкой по их популярности
     *
     * @param userId  — идентификатор пользователя, запрашивающего информацию
     * @param friendId  — идентификатор пользователя, с которым необходимо сравнить список фильмов
     * @return возвращает список общих с другом фильмов с сортировкой по их популярности
     */
    @GetMapping("/common")
    public List<Film> getPopularFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
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

    /**
     * поиск по названию фильмов и по режиссёру
     *
     * @param query — текст для поиска,
     * @param by    — может принимать значения director (поиск по режиссёру), title (поиск по названию),
     *              либо оба значения через запятую при поиске одновременно и по режиссеру и по названию.
     * @return возвращает список фильмов с количеством лайков (От большего к меньшему)
     */
    @GetMapping("/search")
    public List<Film> getSearchResult(@RequestParam String query, @RequestParam String by) {
        return filmService.getSearchResult(query, by);
    }
}
