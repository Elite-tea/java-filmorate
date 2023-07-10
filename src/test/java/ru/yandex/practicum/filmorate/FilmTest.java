package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmTest {
    private final FilmController filmController = new FilmController();
    private final Film film = Film.builder().id(1).name("Стартап").description(
            "Российский фильм-биография, повествующий о создании с нуля крупного российского поискового" +
                    " портала «Холмс». Согласно первоначальной задумке, фильм снимался как история компании «Яндекс».")
            .releaseDate(LocalDate.of(2014, 4, 3)).duration(92).build();

    @Test
    void nameEmpty() { // Если название пустое
        film.setName("");

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilms(film));
        Assertions.assertEquals(0, filmController.getFilm().size());
    }

    @Test
    void descriptionMore200() { // Если описание более 200
        film.setDescription("«Стартап» — российский фильм-биография, повествующий о создании с нуля крупного российского"
                + " поискового портала «Холмс». Согласно первоначальной задумке, фильм снимался как история компании" +
                " «Яндекс». Прототипами главных героев послужили Аркадий Волож и Илья Сегалович. " +
                "Постановку фильма начинал режиссёр Роман Каримов, " +
                "но из-за конфликтов на съёмочной площадке покинул проект. " +
                "В итоге его имя в титрах заменено на «Игорь Сколков». Кроме него, " +
                "в выходных данных фильма режиссёром указан исполнитель главной роли Евгений Ткачук." +
                "Российская премьера состоялась 3 апреля 2014 года.");

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilms(film));
    }

    @Test
    void description200() { // Если описание 200
        film.setDescription("Фильм повествующий о создании с нуля крупного российского поискового портала. " +
                "Согласно задумке, фильм снимался как история компании «Яндекс».Прототипами главных героев стали А. " +
                "Волож и Илья Сегалович.");

        Assertions.assertEquals(film.getDescription(), "Фильм повествующий о создании с " +
                "нуля крупного российского поискового портала. " +
                "Согласно задумке, фильм снимался как история компании «Яндекс».Прототипами главных героев стали А. " +
                "Волож и Илья Сегалович.");
    }

    @Test
    void description0() { // Если описание 0
        film.setDescription("");

        Assertions.assertEquals(film.getDescription(), "");
    }

    @Test
    void dataRelease() { // Если дата старше 28.12.1895
        film.setReleaseDate(LocalDate.of(1894, 11, 5));

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilms(film));
    }

    @Test
    void dataReleaseActual() { // Если дата ровно 28.12.1895
        film.setReleaseDate(LocalDate.of(1895, 12, 28));

        Assertions.assertEquals(film.getReleaseDate(), LocalDate.of(1895, 12, 28));
    }

    @Test
    void filmDuration() { // Продолжительность 0
        film.setDuration(0);

        Assertions.assertEquals(film.getDuration(), 0);
    }

    @Test
    void filmDurationNegative() { // Продолжительность отрицательная
        film.setDuration(-150);

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilms(film));
    }

}