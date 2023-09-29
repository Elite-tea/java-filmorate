package ru.yandex.practicum.filmorate.storage.dao.rate;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Component
public class RateDaoImpl implements RateDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void rateFilm(Long filmId, Long userId, Integer rate) {
        log.debug("rateFilm({}, {}, {})", filmId, userId, rate);
        jdbcTemplate.update("INSERT INTO rate (film_id, user_id, rate) VALUES (?,?,?)", filmId, userId, rate);
        log.trace("Добавлена оценка фильму {} от пользователя {}: {}", filmId, userId, rate);
    }

    @Override
    public void updateFilmRate(Long filmId, Long userId, Integer rate) {
        log.debug("updateFilmRate({}, {}, {})", userId, filmId, rate);
        jdbcTemplate.update("UPDATE rate SET rate=? WHERE user_id=? AND film_id=?", rate, userId, filmId);
        log.trace("Обновлена оценка у фильма {} от пользователя {}: {}", filmId, userId, rate);
    }

    @Override
    public void deleteFilmRate(Long userId, Long filmId) {
        log.debug("deleteFilmRate({}, {})", userId, filmId);
        jdbcTemplate.update("DELETE FROM rate WHERE user_id=? AND film_id=?", userId, filmId);
        log.trace("Удалена оценка у фильма {} от пользователя {}", filmId, userId);
    }

    @Override
    public double checkRates(Long filmId) {
        log.debug("checkRates({})", filmId);
        try {
            Optional<Double> rate = Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT AVG(rate) FROM rate WHERE film_id=?", Double.class, filmId));
            if (rate.isPresent()) {
                log.trace("Подсчитаны оценки фильма: {}", filmId);
                return rate.get();
            }
        } catch (EmptyResultDataAccessException e) {
            return 0.0;
        }
        return 0.0;
    }
}
