package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mapper.MpaMapper;

import java.util.List;

@AllArgsConstructor
@Component
public class MpaDaoImpl implements MpaDao {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpaById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM mpa WHERE mpa_id=?", new MpaMapper(), id);
    }

    @Override
    public List<Mpa> getListMpa() {
        return jdbcTemplate.query("SELECT * FROM mpa ORDER BY mpa_id", new MpaMapper());
    }
}
