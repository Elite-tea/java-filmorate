package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Маппер для реализации сущности Genre из данных полученных в БД
 */
public class GenreMapper implements RowMapper<Genre> {

    /**
     * Метод преобразования данных из БД в сущность
     *
     * @return возвращает сущность Genre
     */
    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("genre_name"));
        return genre;
    }
}
