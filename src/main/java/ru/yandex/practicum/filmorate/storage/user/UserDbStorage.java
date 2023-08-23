package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;
import ru.yandex.practicum.filmorate.validation.Validation;

import javax.validation.Valid;
import java.sql.Date;
import java.util.Collection;

@Slf4j
@Component("UserDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    /**
     * Метод добавления пользователя.
     *
     * @param user информация о пользователе.
     * @return возвращает созданного пользователя
     * @throws NotFoundException генерирует 404 ошибку в случае если пользователь с электронной почтой уже зарегистрирован.
     */
    @Override
    public User create(@Valid @RequestBody User user) {
        Validation.validationUser(user);
        jdbcTemplate.update("INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)", user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()));
        return jdbcTemplate.queryForObject("SELECT user_id, email, login, name, birthday " + "FROM users " + "WHERE email=?", new UserMapper(), user.getEmail());
    }

    @Override
    public User put(User user) {
        Long userId = user.getId();
        if (!getByIdUser(userId).getEmail().isEmpty()) {
            jdbcTemplate.update("UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?", user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()), user.getId());
            log.debug("Пользователь обновлен");
            return user;
        } else {
            log.debug("Пользователь не существует");
            throw new NotFoundException(String.format("Пользователя с id %s не существует", userId));
        }
    }

    @Override
    public Collection<User> getUser() {
        return jdbcTemplate.query("SELECT * FROM users", new UserMapper());
    }

    @Override
    public User getByIdUser(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = ?", new UserMapper(), id);
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException(String.format("Пользователя с id %s не существует", id));
        }
    }

}