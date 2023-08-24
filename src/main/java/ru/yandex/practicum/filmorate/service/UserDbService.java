package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendDao;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.dao.LikeDao;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс-сервис с логикой для оперирования пользователями с хранилищами <b>userDbStorage<b/>
 */
@Getter
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDbService {
    /**
     * Поле с прошлой версией хранилища пользователей
     */
    private final UserStorage userStorage;
    /**
     * Поле для доступа к операциям с жанрами
     */
    private final GenreDao genreDao;
    /**
     * Поле для доступа к операциям с рейтингом
     */
    private final MpaDao mpaDao;
    /**
     * Поле для доступа к операциям с лайками
     */
    private final LikeDao likeDao;
    /**
     * Поле для доступа к операциям с друзьями
     */
    private final FriendDao friendDao;

    /**
     * Конструктор сервиса.
     *
     * @see UserDbService#UserDbService(UserDbStorage, GenreDao, MpaDao, LikeDao, FriendDao)
     */
    @Autowired
    public UserDbService(@Qualifier("UserDbStorage") UserDbStorage userStorage,
                         GenreDao genreDao,
                         MpaDao mpaDao,
                         LikeDao likeDao,
                         FriendDao friendDao) {

        this.userStorage = userStorage;
        this.genreDao = genreDao;
        this.mpaDao = mpaDao;
        this.likeDao = likeDao;
        this.friendDao = friendDao;
    }

    /**
     * Добавление в друзья.
     *
     * @param userId   айди пользователя, добавляющего в друзья.
     * @param idFriend айди добавляемого пользователя в друзья.
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id пользователя или друга.
     */
    public void addFriends(Long userId, Long idFriend) {
        if (userId > 0 && idFriend > 0) {
            boolean status = friendDao.statusFriend(userId, idFriend);
            friendDao.addFriends(userId, idFriend, status);
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
     */
    public void deleteFriends(Long userId, Long idFriend) {

        friendDao.deleteFriends(userId, idFriend);
        log.info("Пользователь с id {} и {} удалены друг у друга из друзей", userId, idFriend);

    }

    /**
     * Получение списка общих друзей у двух пользователей.
     *
     * @param userId   айди пользователя, от кого поступает запрос на получение информации.
     * @param idFriend айди пользователя, с кем необходимо отобразить общих друзей.
     * @return возвращает список общих друзей или пустой список, если таковых необнаружено.
     */
    public List<User> getMutualFriends(Long userId, Long idFriend) {
        List<User> userFriends = getFriends(userId);
        List<User> friendFriends = getFriends(idFriend);

        log.info("Запрошены общие друзья у пользователя с id {} и {}", userId, idFriend);

        return friendFriends.stream()
                .filter(userFriends::contains)
                .filter(friendFriends::contains)
                .collect(Collectors.toList());

    }


    /**
     * Получение списка друзей у пользователя.
     *
     * @param id айди пользователя, чьих друзей необходимо вывести.
     * @return возвращает список друзей или пустой список если их нет.
     * @throws NotFoundException генерирует 404 ошибку в случае если пользователя не существует.
     */
    public List<User> getFriends(Long id) {
        if (userStorage.getByIdUser(id).getEmail().isEmpty()) {

            throw new NotFoundException(String.format("Пользователь с id %s не существует", id));

        }
            log.info("Запрошены друзья у пользователя с id {}", id);

            return friendDao.getFriend(id).stream()
                    .mapToLong(Long::valueOf)
                    .mapToObj(userStorage::getByIdUser)
                    .collect(Collectors.toList());
    }
}
