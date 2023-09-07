package ru.yandex.practicum.filmorate.storage.dao.rate;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@AllArgsConstructor
@Component
public class RateDaoImpl implements RateDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void rateFilm(Long userId, Long filmId, Integer rate) {
        log.debug("rateFilm({}, {}, {})", userId, filmId, rate);
        jdbcTemplate.update("INSERT INTO rates (user_id, film_id, rate) VALUES (?,?,?)", userId, filmId, rate);
        log.trace("Добавлена оценка фильму {} от пользователя {}: {}", filmId, userId, rate);
    }

    @Override
    public double checkRates(Long filmId) {
        log.debug("checkRates({})", filmId);
        double rate = Objects.requireNonNull(jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM rates WHERE film_id=?", Double.class, filmId));
        log.trace("Подсчитаны оценки фильма: {}", filmId);
        return rate;
    }
}
