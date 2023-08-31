package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.friend.FriendDao;
import ru.yandex.practicum.filmorate.storage.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.storage.dao.like.LikeDao;
import ru.yandex.practicum.filmorate.storage.dao.mpa.MpaDao;
import ru.yandex.practicum.filmorate.storage.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.user.UserStorage;

import java.util.Collection;
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
     * Поле для доступа к операциям с фильмами
     */
    private final FilmDbService filmService;

    /**
     * Конструктор сервиса.
     *
     * @see UserDbService#UserDbService(UserDbStorage, GenreDao, MpaDao, LikeDao, FriendDao, FilmDbService)
     */
    @Autowired
    public UserDbService(@Qualifier("UserDbStorage") UserDbStorage userStorage,
                         GenreDao genreDao,
                         MpaDao mpaDao,
                         LikeDao likeDao,
                         FriendDao friendDao,
                         FilmDbService filmService) {

        this.userStorage = userStorage;
        this.genreDao = genreDao;
        this.mpaDao = mpaDao;
        this.likeDao = likeDao;
        this.friendDao = friendDao;
        this.filmService = filmService;
    }

    /**
     * Добавление в друзья.
     *
     * @param userId   id пользователя, добавляющего в друзья.
     * @param idFriend id добавляемого пользователя в друзья.
     * @throws NotFoundException генерирует ошибку 404 если введен не верный id пользователя или друга.
     */
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

    /**
     * Удаление из друзей.
     *
     * @param userId   id пользователя, удаляющего из друзей.
     * @param idFriend id удаляемого пользователя из друзей.
     */
    public void deleteFriend(Long userId, Long idFriend) {
        friendDao.deleteFriend(userId, idFriend);
        log.info("Пользователь с id {} и {} удалены друг у друга из друзей", userId, idFriend);
    }

    /**
     * Получение списка общих друзей у двух пользователей.
     *
     * @param userId   id пользователя, от кого поступает запрос на получение информации.
     * @param idFriend id пользователя, с кем необходимо отобразить общих друзей.
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
     * @param id id пользователя, чьих друзей необходимо вывести.
     * @return возвращает список друзей или пустой список если их нет.
     * @throws NotFoundException генерирует 404 ошибку в случае если пользователя не существует.
     */
    public List<User> getFriends(Long id) {
        if (userStorage.getUserById(id).getEmail().isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id %s не существует", id));
        }
            log.info("Запрошены друзья у пользователя с id {}", id);
        return friendDao.getFriend(id).stream()
                    .mapToLong(Long::valueOf)
                    .mapToObj(userStorage::getUserById)
                    .collect(Collectors.toList());
    }
    
    /**
     * Метод предоставляет рекомендуемые фильмы для пользователя.
     * Точность таргета зависит от активности пользователя.
     *
     * @param id id пользователя для которого запрашиваются рекомендации.
     * @return возвращает список рекомендуемых фильмов или пустой список если таргет недостаточно обогащен.
     * @throws NotFoundException генерирует 404 ошибку в случае если пользователь не зарегистрирован.
     */
    public List<Film> getRecommendations(long id) {
        if (userStorage.getUserById(id).getEmail().isEmpty()) {
            throw new NotFoundException(String.format("пользователь с id %s не зарегистрирован.", id));
        } else {
            log.info("Запрошены рекомендации для пользователя с id {}", id);
            final Collection<Film> userFilms = filmService.getFilmsByUser(id);
            long userId = 0;
            long countCoincidences = 0;
            
            for(User user : userStorage.getUsers()) {
                if (user.getId() != id) {
                    long count = 0;
                    for (Film film : filmService.getFilmsByUser(user.getId())) {
                        if (userFilms.contains(film)) {
                            count++;
                        }
                    }
                    if (count > countCoincidences) {
                        userId = user.getId();
                        countCoincidences = count;
                    }
                }
            }
            log.info("Рекомендации для пользователя с id {} успешно предоставлены.", id);
            return filmService.getFilmsByUser(userId).stream()
                    .filter(film -> !userFilms.contains(film))
                    .collect(Collectors.toList());
        }
    }
}
