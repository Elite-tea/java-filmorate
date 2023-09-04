package ru.yandex.practicum.filmorate.storage.dao.feed;

import ru.yandex.practicum.filmorate.assistant.EventType;
import ru.yandex.practicum.filmorate.assistant.Operation;
import ru.yandex.practicum.filmorate.model.Feed;

import java.time.LocalDateTime;
import java.util.Collection;

public interface FeedStorage {
	void addFeed(LocalDateTime time, Long userId, EventType eventType, Operation operation, Long entityId);
	Collection<Feed> getFeeds(Long id);
}
