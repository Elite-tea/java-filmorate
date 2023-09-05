package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.assistant.EventType;
import ru.yandex.practicum.filmorate.assistant.Operation;
import ru.yandex.practicum.filmorate.model.Feed;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Маппер для реализации сущности Feed полученной из базы данных.
 */
public class FeedMapper implements RowMapper<Feed> {
	/**
	 * Метод преобразования данных.
	 *
	 * @param rs данные для обработки.
	 * @return возвращфет сущность Feed.
	 */
	@Override
	public Feed mapRow(ResultSet rs, int rowNum) throws SQLException {
		return Feed.builder()
				.timestamp(rs.getTimestamp("time").toInstant().toEpochMilli())
				.userId(rs.getLong("user_id"))
				.eventType(EventType.valueOf(rs.getString("event_type")))
				.operation(Operation.valueOf((rs.getString("operation"))))
				.eventId(rs.getLong("event_id"))
				.entityId(rs.getLong("entity_id"))
				.build();
	}
}
