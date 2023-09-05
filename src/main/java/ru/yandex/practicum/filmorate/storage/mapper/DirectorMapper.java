package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Маппер для реализации сущности Director из данных полученных в БД
 */
public class DirectorMapper implements RowMapper<Director> {

    /**
     * Метод преобразования данных из БД в POJO сущность
     *
     * @return возвращает сущность Director
     */
    @Override
    public Director mapRow(ResultSet rs, int rowNum) throws SQLException {
        Director director = new Director();
        director.setId(rs.getInt("director_id"));
        director.setName(rs.getString("director_name"));
        return director;
    }
}
