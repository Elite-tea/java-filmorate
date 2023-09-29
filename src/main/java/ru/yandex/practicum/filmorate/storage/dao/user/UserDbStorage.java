package ru.yandex.practicum.filmorate.storage.dao.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component("UserDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Метод добавления пользователя.
     *
     * @param user информация о пользователе.
     * @return возвращает созданного пользователя
     * @throws NotFoundException генерирует 404 ошибку в случае если пользователь с электронной почтой
     * уже зарегистрирован.
     */
    @Override
    public User create(User user) {
        jdbcTemplate.update("INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)",
                user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()));
        return jdbcTemplate.queryForObject("SELECT user_id, email, login, name, birthday FROM users " +
                "WHERE email=?", new UserMapper(), user.getEmail());
    }

    /**
     * Метод обновления пользователя.
     *
     * @param user информация о пользователе.
     * @return возвращает созданного пользователя
     * @throws NotFoundException генерирует 404 ошибку в случае если пользователь с электронной почтой
     * уже зарегистрирован.
     */
    @Override
    public User update(User user) {
        Long userId = user.getId();
        if (!getUserById(userId).getEmail().isEmpty()) {
            jdbcTemplate.update("UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?",
                    user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()), user.getId());
            log.debug("Пользователь обновлен");
            return user;
        } else {
            log.debug("Пользователь не существует");
            throw new NotFoundException(String.format("Пользователя с id %d не существует", userId));
        }
    }

    /**
     * Метод удаляет пользователя из базы данных
     *
     * @param userId идентификатор удаляемого пользователя
     */
    @Override
    public void deleteUser(Long userId) {
        try {
            jdbcTemplate.update("DELETE FROM users WHERE user_id=?",userId);
            log.debug("Пользователь удален");
        } catch (NotFoundException e) {
            log.debug("Пользователь не существует");
            throw new NotFoundException(String.format("Пользователя с id %d не существует", userId));
        }
    }

    /**
     * Метод для получения списка пользователей
     */
    @Override
    public Collection<User> getUsers() {
        return jdbcTemplate.query("SELECT * FROM users", new UserMapper());
    }

    /**
     * Метод получения пользователя по идентификатору
     *
     * @param id идентификатор пользователя
     * @return возвращает найденного пользователя
     * @throws NotFoundException генерирует 404 ошибку в случае если пользователь не найден
     */
    @Override
    public User getUserById(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = ?", new UserMapper(), id);
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException(String.format("Пользователя с id %d не существует", id));
        }
    }

    /**
     * Метод совершает запрос к базе и осуществляет таргетированный поиск пользователя с похожими вкусами.
     * Фильмы с оценкой ниже 5 не попадают в систему учета, поскольку это считается низкой оценкой.
     *
     * @param id id пользователя для которого осуществляется поиск.
     * @return возвращает пользователя с похожими вкусами.
     */
    @Override
    public Optional<User> getTargetUser(Long id) {
        return jdbcTemplate.query("SELECT * FROM USERS WHERE USER_ID IN " +
                "(SELECT USER_ID FROM RATE WHERE film_id IN " +
                "(SELECT FILM_ID FROM RATE WHERE USER_ID  = ? " +
                "INTERSECT SELECT FILM_ID FROM RATE) " +
                "AND NOT USER_ID = ? " +
                "AND rate > 5 " +
                "GROUP BY USER_ID " +
                "ORDER BY COUNT(film_id) DESC " +
                "LIMIT 1)", new UserMapper(), id, id).stream().findFirst();
    }
}