package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaDbService;

import java.util.Collection;

/**
 * Класс-контроллер для получения данных о рейтинге, а так же реализации API со свойством <b>mpaService</b>.
 */
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaDbService mpaService;

    /**
     * Запрос рейтинга по id
     *
     * @param id айди запрашиваемого рейтинга
     * @return возвращает объект рейтинга
     */
    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable Integer id) {
        return mpaService.getMpaById(id);
    }

    /**
     * Запрос списка рейтингов
     *
     * @return возвращает коллекцию рейтингов
     */
    @GetMapping
    public Collection<Mpa> getMpaList() {
        return mpaService.getListMpa();
    }
}