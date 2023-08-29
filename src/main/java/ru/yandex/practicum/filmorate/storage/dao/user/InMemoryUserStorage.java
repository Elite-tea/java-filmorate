package ru.yandex.practicum.filmorate.storage.dao.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.Validation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс-хранилище реализующий интерфейс {@link UserStorage} для хранения и обновления пользователей со свойствами <b>users<b/> и <b>id<b/>
 */
@Component("InMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    /**
     * Поле хранилище пользователей
     */
    private final Map<Long, User> users = new HashMap<>();
    /**
     * Поле счетчик идентификаторов пользователей
     */
    private Long id = 1L;

    /**
     * Метод добавления пользователя.
     *
     * @param user информация о пользователе.
     * @return возвращает созданного пользователя
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

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public Collection<User> getUsers() {
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    /**
     * Метод обновления пользователя.
     *
     * @param user информация о пользователе.
     * @return возвращает обновленного пользователя
     * @throws NotFoundException генерирует 404 ошибку в случае если пользователя не существует.
     */
    public User put(@Valid @RequestBody User user) {
        Long userId = user.getId();
        Validation.validationUser(user);
        if (users.containsKey(userId)) {
            log.debug("Пользователь обновлен");
            users.put(userId, user);
        } else {
            log.debug("Пользователь не существует");
            throw new NotFoundException(String.format("Пользователя с id %s не существует", userId));
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
     * Метод получение пользователя по id.
     *
     * @param id айди пользователя
     * @return возвращает пользователя с указанным id.
     * @throws NotFoundException генерирует 404 ошибку в случае если пользователя не существует.
     */
    public User getByIdUser(Long id) {
        if (users.containsKey(id)) {
            log.debug("Запрошен пользователь c id: {}", id);
            return users.get(id);
        } else {
            log.debug("Пользователь не существует");
            throw new NotFoundException(String.format("Пользователя с id %s не существует", id));
        }
    }

}
