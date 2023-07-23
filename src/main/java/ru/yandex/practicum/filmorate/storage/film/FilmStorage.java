package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film addFilms(Film film);

    Film put(Film film);

    Collection<Film> getFilm();

    Film getByIdFilm(Long id);
}
