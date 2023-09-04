package ru.yandex.practicum.filmorate.storage.dao.feed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.assistant.EventType;
import ru.yandex.practicum.filmorate.assistant.Operation;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.storage.mapper.FeedMapper;

import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedDbStorage implements FeedStorage {
	private final JdbcTemplate jdbcTemplate;
	@Override
	public void addFeed(LocalDateTime time, Long userId, EventType eventType, Operation operation, Long entityId) {
		jdbcTemplate.update("INSERT INTO feed (time, user_id, event_type, operation, entity_id)" +
				"VALUES (?, ?, ?, ?, ?);",
				time, userId, eventType.toString(), operation.toString(), entityId);
	}
	
	@Override
	public Collection<Feed> getFeeds(Long id) {
		/*return jdbcTemplate
				.query("SELECT * FROM feed WHERE user_id IN (SELECT friend_id FROM friends WHERE user_id = ?)",
						new FeedMapper(), id);*/
		return jdbcTemplate
				.query("SELECT * FROM feed WHERE user_id = ?", new FeedMapper(), id);
	}
}
