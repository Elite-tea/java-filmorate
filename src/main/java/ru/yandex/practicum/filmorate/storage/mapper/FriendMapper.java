package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Friend;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Маппер для создания POJO сущности Friend из данных полученных в БД
 */
public class FriendMapper implements RowMapper<Friend> {

    /**
     * Метод преобразования данных из БД в сущность POJO - Friend
     *
     * @return возвращает сущность Friend
     */
    @Override
    public Friend mapRow(ResultSet rs, int rowNum) throws SQLException {
        Friend friend = new Friend();
        friend.setUserId(rs.getLong("user_id"));
        friend.setFriendId(rs.getLong("friend_id"));
        friend.setStatus(rs.getBoolean("status"));
        return friend;
    }
}