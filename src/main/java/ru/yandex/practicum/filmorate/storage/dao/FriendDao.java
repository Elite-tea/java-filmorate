package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;

/**
 * Интерфейс для работы с логикой касающиеся дружбы, реализован в {@link GenreDaoImpl}
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
     * Получение списка общих друзей у двух пользователей.
     *
     * @param userId   айди пользователя, от кого поступает запрос на получение информации.
     * @param idFriend айди пользователя, с кем необходимо отобразить общих друзей.
     * @return возвращает список общих друзей или пустой список, если таковых необнаружено.
     */
    List<Friend> getMutualFriends(Long userId, Long idFriend);

    /**
     * Получение списка друзей у пользователя.
     *
     * @param id айди пользователя, чьих друзей необходимо вывести.
     * @return возвращает список друзей или пустой список если их нет.
     * @throws NotFoundException генерирует 404 ошибку в случае если пользователя не существует.
     */
    List<Friend> getFriends(Long id);

    boolean statusFriend(Long userId, Long friendId);

    List<Long> getFriend(Long userId);
}