package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.assistant.EventType;
import ru.yandex.practicum.filmorate.assistant.Operation;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.feed.FeedStorage;
import ru.yandex.practicum.filmorate.storage.dao.friend.FriendDao;
import ru.yandex.practicum.filmorate.storage.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
	 * Поле для доступа к операциям с фильмами
	 */
	private final FilmDbService filmService;
	/**
	 * Поле для доступа к операциям с лентой событий.
	 */
	private final FeedStorage feedStorage;

	/**
	 * Конструктор сервиса.
	 *
	 * @see UserDbService#UserDbService(UserDbStorage, FriendDao, FilmDbService, FeedStorage)
	 */
	@Autowired
	public UserDbService(@Qualifier("UserDbStorage") UserDbStorage userStorage,
						 FriendDao friendDao,
						 FilmDbService filmService,
						 FeedStorage feedStorage) {

		this.userStorage = userStorage;
		this.friendDao = friendDao;
		this.filmService = filmService;
		this.feedStorage = feedStorage;
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
			feedStorage.addFeed(LocalDateTime.now(), userId, EventType.FRIEND, Operation.ADD, idFriend);
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
		feedStorage.addFeed(LocalDateTime.now(), userId, EventType.FRIEND, Operation.REMOVE, idFriend);
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
	 * Метод предоставляет рекомендуемые фильмы для пользователя.
	 * Точность таргета зависит от активности пользователя.
	 *
	 * @param id id пользователя для которого запрашиваются рекомендации.
	 * @return возвращает список рекомендуемых фильмов или пустой список если таргет недостаточно обогащен.
	 * @throws NotFoundException генерирует 404 ошибку в случае если пользователь не зарегистрирован.
	 */
	public List<Film> getRecommendations(long id) {
		if (userStorage.getUserById(id) == null) {
			throw new NotFoundException(String.format("пользователь с id %d не зарегистрирован.", id));
		} else {
			log.info("Запрошены рекомендации для пользователя с id {}", id);
			final Collection<Film> userFilms = filmService.getFilmsByUser(id);
			long userId = 0;
			long countCoincidences = 0;
			for (User user : userStorage.getUsers()) {
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
			log.info("Рекомендации для пользователя с id {} успешно предоставлены", id);
			return filmService.getFilmsByUser(userId).stream()
					.filter(film -> !userFilms.contains(film))
					.collect(Collectors.toList());
		}
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

	public void deleteUser(Long userId) {
		userStorage.deleteUser(userId);
	}
	
	/**
	 * Метод возвращения ленты событий пользователя.
	 *
	 * @param userId id пользователя для которого выгружается лента событий.
	 * @return возвращает ленту событий в которых приняли участие друзья пользователя.
	 */
	public Collection<Feed> getFeeds(Long userId) {
		if (userStorage.getUserById(userId) != null) {
			log.info("Запрошена лента событий для пользователя с id {}", userId);
			return new ArrayList<>(feedStorage.getFeeds(userId));
		} else {
			throw new NotFoundException(String.format("пользователь с id %d не зарегистрирован.", userId));
		}
	}
}
