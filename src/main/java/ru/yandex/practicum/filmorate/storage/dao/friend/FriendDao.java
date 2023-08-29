package ru.yandex.practicum.filmorate.storage.dao.friend;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;

/**
 * Интерфейс для работы с логикой касающиеся дружбы, реализован в {@link FriendDaoImpl}
 */
public interface FriendDao {
    /**
     * Добавление в друзья.
     *
     * @param userId   id пользователя, добавляющего в друзья.
     * @param idFriend id добавляемого пользователя в друзья.
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id пользователя или друга.
     */
    void addFriends(Long userId, Long idFriend, boolean status);

    /**
     * Удаление из друзей.
     *
     * @param userId   id пользователя, удаляющего из друзей.
     * @param idFriend id удаляемого пользователя из друзей.
     * @throws NotFoundException генерирует ошибку 404 если пользователей с id userId и idFriend не существует.
     */
    void deleteFriend(Long userId, Long idFriend);

    /**
     * Получение статуса пользователя
     *
     * @param userId id пользователя, кто добавляет в друзья.
     * @param friendId id пользователя, кого добавляют в друзья.
     * @return возвращает true если добавили в друзья или false если заявка отправлена
     * @throws NotFoundException генерирует 404 ошибку в случае если пользователя не существует.
     */
    boolean isFriend(Long userId, Long friendId);

    /**
     * Получение списка друзей у пользователя.
     *
     * @param userId id пользователя, чьих друзей необходимо вывести.
     * @return возвращает список друзей или пустой список если их нет.
     * @throws NotFoundException генерирует 404 ошибку в случае если пользователя не существует.
     */
    List<Long> getFriend(Long userId);
}