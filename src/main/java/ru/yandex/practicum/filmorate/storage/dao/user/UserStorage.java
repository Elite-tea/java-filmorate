package ru.yandex.practicum.filmorate.storage.dao.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

/**
 * Интерфейс для работы с хранилищем пользователей, реализован в {@link InMemoryUserStorage} и {@link UserDbStorage}
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
    User update(User user);

    /**
     * Метод удаления пользователя по идентификатору.
     *
     * @param id идентификатор удаляемого пользователя
     */
    void deleteUser(Long id);

    /**
     * Метод запроса пользователей
     *
     * @return возвращает коллекцию пользователей
     */
    Collection<User> getUsers();

    /**
     * Метод запроса пользователя по id
     *
     * @param id идентификатор пользователя
     * @return возвращает пользователя по id
     */
    User getUserById(Long id);

}
