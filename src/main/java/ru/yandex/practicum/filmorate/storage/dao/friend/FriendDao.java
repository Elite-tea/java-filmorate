package ru.yandex.practicum.filmorate.storage.dao.friend;

import ru.yandex.practicum.filmorate.Exception.NotFoundException;

import java.util.List;

/**
 * Интерфейс для работы с логикой касающиеся дружбы, реализован в {@link FriendDaoImpl}
 */
public interface FriendDao {
    /**
     * Добавление в друзья.
     *
     * @param userId   айди пользователя, добавляющего в друзья.
     * @param idFriend айди добавляемого пользователя в друзья.
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id пользователя или друга.
     */
    void addFriends(Long userId, Long idFriend, boolean status);

    /**
     * Удаление из друзей.
     *
     * @param userId   айди пользователя, удаляющего из друзей.
     * @param idFriend айди удаляемого пользователя из друзей.
     * @throws NotFoundException генерирует ошибку 404 если пользователей с id userId и idFriend не существует.
     */
    void deleteFriends(Long userId, Long idFriend);

    /**
     * Получение статуса пользователя
     *
     * @param userId айди пользователя, кто добавляет в друзья.
     * @param friendId айди пользователя, кого добавляют в друзья.
     * @return возвращает true если добавили в друзья или false если заявка отправлена
     * @throws NotFoundException генерирует 404 ошибку в случае если пользователя не существует.
     */
    boolean statusFriend(Long userId, Long friendId);

    /**
     * Получение списка друзей у пользователя.
     *
     * @param userId айди пользователя, чьих друзей необходимо вывести.
     * @return возвращает список друзей или пустой список если их нет.
     * @throws NotFoundException генерирует 404 ошибку в случае если пользователя не существует.
     */
    List<Long> getFriend(Long userId);
}