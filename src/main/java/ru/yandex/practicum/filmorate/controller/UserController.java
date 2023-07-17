package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.Validation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    /**
     * Добавление пользователя.
     *
     * @param user информация о пользователе.
     */

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        Validation.validationUser(user);
        if (users.containsKey(user.getId())) {
            log.debug("Email уже существует");
            throw new ValidationException(String.format("Пользователь с электронной почтой %s уже зарегистрирован.", user.getEmail()));
        }

        log.debug("Пользователь создан");
        user.setId(id);
        id++;
        users.put(user.getId(), user);
        return user;
    }

    /**
     * Обновление пользователя.
     *
     * @param user информация о пользователе.
     */

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        Validation.validationUser(user);
        if (users.containsKey(user.getId())) {
            log.debug("Пользователь обновлен");
            users.put(user.getId(), user);
        } else {
            log.debug("Пользователь не существует");
            throw new ValidationException(String.format("Пользователя с id %s не существует", user.getId()));
        }
        return user;
    }

    /**
     * Получение списка пользователей.
     *
     * @return users возвращает коллекцию пользователей.
     */

    @GetMapping
    public Collection<User> getUser() {
        log.debug("Запрошен список пользователей, их количество: {}", users.size());
        return users.values();
    }
}
