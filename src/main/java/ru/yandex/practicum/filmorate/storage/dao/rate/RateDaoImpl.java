package ru.yandex.practicum.filmorate.storage.dao.rate;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Component
public class RateDaoImpl implements RateDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void rateFilm(Long filmId, Long userId,  Integer rate) {
        log.debug("rateFilm({}, {}, {})", filmId, userId, rate);
        jdbcTemplate.update("INSERT INTO rate (user_id, film_id, rate) VALUES (?,?,?)", filmId, userId, rate);
        log.trace("Добавлена оценка фильму {} от пользователя {}: {}", filmId, userId, rate);
    }

    @Override
    public double checkRates(Long filmId) {
        log.debug("checkRates({})", filmId);
        try {
            Optional<Double> rate = Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT avg(rate) FROM rate WHERE film_id=?", Double.class, filmId));
            if(rate.isPresent()) {
                log.trace("Подсчитаны оценки фильма: {}", filmId);
                return rate.get();
            }
        } catch(EmptyResultDataAccessException e) {
            return 0.0;
        }
        return 0.0;
    }
}
