package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс-сервис с логикой для оперирования друзьями у пользователя с хранилищем <b>userStorage<b/>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    /**
     * Поле хранилище пользователей
     */
    private final UserStorage userStorage;


    /**
     * Добавление в друзья.
     *
     * @param userId   айди пользователя, добавляющего в друзья.
     * @param idFriend айди добавляемого пользователя в друзья.
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id пользователя или друга.
     */
    public void addFriends(Long userId, Long idFriend) {
        if (userId > 0 && idFriend > 0) {
            userStorage.getByIdUser(userId).addFriend(idFriend);
            userStorage.getByIdUser(idFriend).addFriend(userId);

            log.info("Пользователи с id {} и {} добавлены друг другу в друзья", userId, idFriend);
        } else {
            throw new NotFoundException(String.format("Введен не верный id пользователя %s или друга %s", userId, idFriend));
        }
    }

    /**
     * Удаление из друзей.
     *
     * @param userId   айди пользователя, удаляющего из друзей.
     * @param idFriend айди удаляемого пользователя из друзей.
     * @throws NotFoundException генерирует ошибку 404 если пользователей с id userId и idFriend не существует.
     */
    public void deleteFriends(Long userId, Long idFriend) {
        if (userStorage.getByIdUser(userId) != null && userStorage.getByIdUser(idFriend) != null) {
            userStorage.getByIdUser(userId).deleteFriend(idFriend);
            userStorage.getByIdUser(idFriend).deleteFriend(userId);

            log.info("Пользователь с id {} и {} удалены друг у друга из друзей", userId, idFriend);
        } else {
            throw new NotFoundException(String.format("Пользователь с id %s и его друг с id %s не существует", userId, idFriend));
        }
    }

    /**
     * Получение списка общих друзей у двух пользователей.
     *
     * @param userId   айди пользователя, от кого поступает запрос на получение информации.
     * @param idFriend айди пользователя, с кем необходимо отобразить общих друзей.
     * @return возвращает список общих друзей или пустой список, если таковых необнаружено.
     */
    public List<User> getMutualFriends(Long userId, Long idFriend) {
        List<Long> listMutualFriend = new ArrayList<>(userStorage.getByIdUser(userId).getFriends());
        listMutualFriend.addAll(userStorage.getByIdUser(idFriend).getFriends());
        Set<Long> getMutualFriends = new HashSet<>();

        List<User> userList = listMutualFriend
                .stream().filter(e -> !getMutualFriends.add(e))
                .map(userStorage::getByIdUser)
                .collect(Collectors.toList());

        log.info("Запрошены общие друзья у пользователя с id {} и {}", userId, idFriend);

        if (!userList.isEmpty()) {
            return userList;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Получение списка друзей у пользователя.
     *
     * @param id айди пользователя, чьих друзей необходимо вывести.
     * @return возвращает список друзей или пустой список если их нет.
     * @throws NotFoundException генерирует 404 ошибку в случае если пользователя не существует.
     */
    public List<User> getFriends(Long id) {
        if (userStorage.getByIdUser(id) != null) {
            log.info("Запрошены друзья у пользователя с id {}", id);
            return userStorage.getByIdUser(id).getFriends()
                    .stream()
                    .map(userStorage::getByIdUser)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException(String.format("Пользователь с id %s не существует", id));
        }
    }

    /**
     * Получение доступа к хранилищу через сервис.
     */
    public UserStorage getUserStorage() {
        return userStorage;
    }
}