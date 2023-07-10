package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @PostMapping
    public User create(@Valid @RequestBody User user) { //Создание пользователя
        validationUser(user);
        if (users.containsKey(user.getId())) {
            log.debug("Email уже существует");
            throw new ValidationException("Пользователь с электронной почтой " + user.getEmail() + " уже зарегистрирован.");
        }

        log.debug("Пользователь создан");
        user.setId(id);
        id++;
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) { //Обновление пользователя
        validationUser(user);
        if (users.containsKey(user.getId())) {
            log.debug("Пользователь обновлен");
            users.put(user.getId(), user);
        } else {
            log.debug("Пользователь не существует");
            throw new ValidationException("Пользователя с id " + user.getId() + " не существует");
        }
        return user;
    }

    @GetMapping
    public Collection<User> getUser() { //Получение списка пользователей
        log.debug("Запрошен список пользователей, их количество: " + users.size());
        return users.values();
    }

    public void validationUser(User user) {
        char[] nameChar = user.getLogin().toCharArray();

        for (char c : nameChar) {
            if (c == ' ') { // Для наглядности вставим пробел между индексами
                throw new ValidationException("Логин содержит пробел");
            }
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("У пользователя нет имени, используем логин");
            user.setName(user.getLogin());
        }

        if ((user.getLogin() == null || user.getLogin().isBlank())) {
            log.debug("У пользователя нет логина");
            user.setName(user.getLogin());
            throw new ValidationException("Отсутствует или не верный логин");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Не верная ДР");
            throw new ValidationException("Вы путешествуете во времени? Дата рождения не может быть позже" + LocalDate.now());
        }

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Не верный email у пользователя " + user.getId());
        }


    }
}
