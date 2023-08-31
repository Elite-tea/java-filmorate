package ru.yandex.practicum.filmorate.storage.mapper;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Маппер для создания поля useful из данных полученных в БД.
 */
public class FilmReviewMapper implements RowMapper<Integer> {

    /**
     * Метод преобразования данных из БД в поле POJO сущности Review
     *
     * @return возвращает поле useful
     */
    @Override
    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("useful");
    }
}
