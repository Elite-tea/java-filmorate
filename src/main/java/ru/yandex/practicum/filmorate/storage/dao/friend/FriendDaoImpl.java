package ru.yandex.practicum.filmorate.storage.dao.friend;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.storage.mapper.FriendMapper;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class FriendDaoImpl implements FriendDao {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriends(Long userId, Long idFriend, boolean status) {
        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id, status) VALUES (?,?,?)",
                userId, idFriend, status);
    }

    @Override
    public void deleteFriend(Long userId, Long idFriend) {
        jdbcTemplate.update("DELETE FROM friends WHERE user_id = ? AND friend_id = ?", userId, idFriend);
        jdbcTemplate.update("UPDATE friends SET status = false WHERE user_id=? AND friend_id=?", idFriend, userId);
    }

    @Override
    public boolean isFriend(Long userId, Long friendId) {
        try {
            jdbcTemplate.queryForObject(
                    "SELECT user_id, friend_id, status FROM friends WHERE user_id=? AND friend_id=?",
                    new FriendMapper(), userId, friendId);
            return true;
        } catch (EmptyResultDataAccessException exception) {
            return false;
        }
    }

    @Override
    public List<Long> getFriend(Long userId) {
        return jdbcTemplate.query("SELECT user_id, friend_id, status FROM friends WHERE user_id=?",
                 new FriendMapper(), userId)
                .stream()
                .map(Friend::getFriendId)
                .collect(Collectors.toList());
    }
}