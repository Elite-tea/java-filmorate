package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserDbService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


/**
 * Класс-контроллер для создания и редактирования пользователей, а так же реализации API со свойством <b>userService</b>.
 */
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    /**
     * Поле сервис
     */
    private final UserDbService userService;

    /**
     * Добавляет пользователя в хранилище.
     *
     * @param user объект пользователя.
     * @return возвращает добавленного пользователя.
     */
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    /**
     * Обновляет пользователя в хранилище.
     *
     * @param user объект пользователя.
     * @return возвращает измененного пользователя.
     */
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    /**
     * Добавляет пользователя в друзья.
     *
     * @param id       id пользователя кто добавляет.
     * @param friendId id пользователя кого добавляют.
     */
    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    /**
     * Удаляет пользователя из друзей.
     *
     * @param id идентификатор пользователя кто удаляет.
     * @param friendId id пользователя кого удаляют.
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param userId идентификатор удаляемого пользователя.
     */
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.getUserStorage().deleteUser(userId);
    }

    /**
     * Запрашивает всех друзей пользователя.
     *
     * @param id id пользователя чьих друзей запрашиваем.
     * @return возвращает список друзей пользователя.
     */
    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        return userService.getFriends(id);
    }

    /**
     * Запрашивает пользователя по id.
     *
     * @param id id пользователя.
     * @return возвращает пользователя c указанным id.
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * Запрашивает общих друзей у двух пользователей.
     *
     * @param id      id пользователя.
     * @param otherId id второго пользователя.
     * @return возвращает список пользователей, являющихся общими друзьями у пользователей.
     */
    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getMutualFriends(id, otherId);
    }

    /**
     * Запрашивает коллекцию пользователей.
     *
     * @return возвращает коллекцию пользователей.
     */
    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    /**
     * Возвращает рекомендуемый фильм для пользователя.
     *
     * @param id id пользователя.
     * @return возвращает рекомендуемый фильм.
     */
    @GetMapping("/{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable Long id) {
        return userService.getRecommendations(id);
    }
}