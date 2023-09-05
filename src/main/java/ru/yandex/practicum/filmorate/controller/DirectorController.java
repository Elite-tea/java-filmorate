package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorDbService;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс-контроллер для управления сущностями режиссеров и реализации API со свойством <b>directorService</b>.
 */
@RestController
@RequestMapping("/directors")
@AllArgsConstructor
public class DirectorController {
    /**
     * Поле сервис
     */
    private final DirectorDbService directorService;

    /**
     * Добавление режиссера в хранилище.
     *
     * @param director объект режиссера
     * @return возвращает объект добавленного режиссера.
     */
    @PostMapping
    public Director addDirector(@Valid @RequestBody Director director) {
        return directorService.addDirector(director);
    }

    /**
     * Обновление режиссера в хранилище.
     *
     * @param director объект режиссера
     * @return возвращает объект обновленного режиссера.
     */
    @PutMapping
    public Director updateDirectorData(@Valid @RequestBody Director director) {
        return directorService.updateDirector(director);
    }

    /**
     * Получение режиссера из хранилища.
     *
     * @param id идентификатор получаемого пользователя.
     * @return возвращение объекта пользователя.
     */
    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable Integer id) {
        return directorService.getDirectorById(id);
    }

    /**
     * Удаление данных о режиссере по идентификатору.
     *
     * @param id идентификатор удаляемого пользователя.
     */
    @DeleteMapping("/{id}")
    public void deleteDirectorById(@PathVariable Integer id) {
        directorService.deleteDirectorById(id);
    }

    /**
     * Возвращает список режиссеров.
     *
     * @return возвращаемый список режиссеров.
     */
    @GetMapping
    public List<Director> getDirectors() {
        return directorService.getDirectors();
    }
}
