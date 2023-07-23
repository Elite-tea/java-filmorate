package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.Validation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    /**
     * Добавление пользователя.
     *
     * @param user информация о пользователе.
     * @throws NotFoundException генерирует 404 ошибку в случае если пользователь с электронной почтой уже зарегистрирован.
     */


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
     * @throws NotFoundException генерирует 404 ошибку в случае если пользователя не существует.
     */

    public User put(@Valid @RequestBody User user) {
        Validation.validationUser(user);
        if (users.containsKey(user.getId())) {
            log.debug("Пользователь обновлен");
            users.put(user.getId(), user);
        } else {
            log.debug("Пользователь не существует");
            throw new NotFoundException(String.format("Пользователя с id %s не существует", user.getId()));
        }
        return user;
    }

    /**
     * Получение списка пользователей.
     *
     * @return users возвращает коллекцию пользователей.
     */

    public Collection<User> getUser() {
        log.debug("Запрошен список пользователей, их количество: {}", users.size());
        return users.values();
    }

    /**
     * Получение пользователя по id.
     *
     * @param id айди пользователя
     * @throws NotFoundException генерирует 404 ошибку в случае если пользователя не существует.
     */

    public User getByIdUser(Long id) {
        if (users.containsKey(id)) {
            log.debug("Запрошен пользователь: {}", users.get(id));
            return users.get(id);
        } else {
            log.debug("Пользователь не существует");
            throw new NotFoundException(String.format("Пользователя с id %s не существует", users.get(id)));
        }
    }
}
