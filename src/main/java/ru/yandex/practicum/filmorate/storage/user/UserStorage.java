package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User create(User user);

    User put(User user);

    Collection<User> getUser();

    User getByIdUser(Long id);
}
