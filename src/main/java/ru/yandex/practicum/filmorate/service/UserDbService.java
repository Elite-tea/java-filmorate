package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.friend.FriendDao;
import ru.yandex.practicum.filmorate.storage.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс-сервис с логикой для оперирования пользователями с хранилищами <b>userDbStorage<b/>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDbService {
    /**
     * Поле с прошлой версией хранилища пользователей
     */
    private final UserStorage userStorage;
    /**
     * Поле для доступа к операциям с друзьями
     */
    private final FriendDao friendDao;

    /**
     * Конструктор сервиса.
     *
     * @see UserDbService#UserDbService(UserDbStorage, FriendDao)
     */
    @Autowired
    public UserDbService(@Qualifier("UserDbStorage") UserDbStorage userStorage,
                         FriendDao friendDao) {

        this.userStorage = userStorage;
        this.friendDao = friendDao;
    }

    public void addFriend(Long userId, Long idFriend) {
        if (userId > 0 && idFriend > 0) {
            boolean status = friendDao.isFriend(userId, idFriend);
            friendDao.addFriends(userId, idFriend, status);
            log.info("Пользователи с id {} и {} добавлены друг другу в друзья", userId, idFriend);
        } else {
            throw new NotFoundException(String.format("Введен не верный id пользователя %d или друга %d",
                    userId, idFriend));
        }
    }

    public void deleteFriend(Long userId, Long idFriend) {
        friendDao.deleteFriend(userId, idFriend);
        log.info("Пользователь с id {} и {} удалены друг у друга из друзей", userId, idFriend);
    }

    public List<User> getMutualFriends(Long userId, Long idFriend) {
        List<User> userFriends = getFriends(userId);
        List<User> friendFriends = getFriends(idFriend);
        log.info("Запрошены общие друзья у пользователя с id {} и {}", userId, idFriend);
        return friendFriends.stream()
                .filter(userFriends::contains)
                .filter(friendFriends::contains)
                .collect(Collectors.toList());

    }

    public List<User> getFriends(Long id) {
        if (userStorage.getUserById(id).getEmail().isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id %d не существует", id));
        }
            log.info("Запрошены друзья у пользователя с id {}", id);
        return friendDao.getFriend(id).stream()
                    .mapToLong(Long::valueOf)
                    .mapToObj(userStorage::getUserById)
                    .collect(Collectors.toList());
    }

    public User createUser(User user) {
        log.debug("createUser({})", user);
        Validation.validationUser(user);
        User thisUser = userStorage.create(user);
        log.info("Добавлен новый пользователь: {}", user);
        return thisUser;
    }

    public User updateUser(User user) {
        log.debug("updateUser({})", user);
        Validation.validationUser(user);
        User thisUser = userStorage.update(user);
        log.info("Обновлён пользователь: {}", thisUser);
        return thisUser;
    }

    public Collection<User> getUsers() {
        log.debug("getUsers()");
        Collection<User> users = userStorage.getUsers();
        log.info("Возвращён список пользователей: {}", users);
        return users;
    }

    public User getUserById(Long id) {
        log.debug("getUserById({})", id);
        User returnedUser = userStorage.getUserById(id);
        log.info("Возвращён пользователь: {}", returnedUser);
        return returnedUser;
    }
}
