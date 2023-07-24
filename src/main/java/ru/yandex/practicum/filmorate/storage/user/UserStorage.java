package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

/**
 * Интерфейс для работы с хранилищем пользователей, реализован в {@link InMemoryUserStorage}
 */
public interface UserStorage {
    /**
     * Метод добавления пользователя
     *
     * @param user объект пользователя
     * @return возвращает созданного пользователя
     */
    User create(User user);

    /**
     * Метод обновления пользователя
     *
     * @param user объект пользователя
     * @return возвращает обновленного пользователя
     */
    User put(User user);

    /**
     * Метод запроса пользователей
     *
     * @return возвращает коллекцию пользователей
     */
    Collection<User> getUser();

    /**
     * Метод запроса пользователя по id
     *
     * @param id идентификатор пользователя
     * @return возвращает пользователя по id
     */
    User getByIdUser(Long id);
}
