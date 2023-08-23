package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.util.Collection;

@AllArgsConstructor
@Component
public class UserDaoImpl implements UserStorage {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        jdbcTemplate.update("INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)", user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()));

        return jdbcTemplate.queryForObject("SELECT user_id, email, login, name, birthday FROM users WHERE email=?", new UserMapper(), user.getEmail());
    }

    @Override
    public User put(User user) {
        jdbcTemplate.update("UPDATE users SET email=?, login=?, name=?, birthday=?, WHERE user_id=?", user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()), user.getId());
        return user;
    }

    @Override
    public Collection<User> getUser() {
        return jdbcTemplate.query("SELECT * FROM users", new UserMapper());
    }

    @Override
    public User getByIdUser(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = ?", new UserMapper(), id);
    }
}
