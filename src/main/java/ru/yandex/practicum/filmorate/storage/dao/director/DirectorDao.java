package ru.yandex.practicum.filmorate.storage.dao.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Set;

/**
 * Интерфейс для работы с хранилищем режиссеров, реализован в {@link DirectorDaoImpl}
 */
public interface DirectorDao {

    /**
     * Метод добавления режиссера в БД
     *
     * @param director добавляемые данные о сущности режиссера
     * @return возвращается сущность режиссера с присвоенным идентификатором в БД
     */
    Director addDirector(Director director);

    /**
     * Метод добавления режиссеров в фильм
     *
     * @param filmId фильм, к которому добавляются режиссеры
     * @param directors список добавляемых режиссеров
     */
    void addDirectorsToFilm(Long filmId, Set<Director> directors);

    Director updateDirectorData(Director director);

    /**
     * Метод обновления списка режиссеров у фильма
     *
     * @param filmId фильм у которого обновляется список режиссеров
     * @param directors список режиссеров для обновления
     */
    void updateDirectorsInFilm(Long filmId, Set<Director> directors);

    /**
     * Метод получения данных о сущности режиссера по идентификатору
     *
     * @param id идентификатор сущности режиссера
     * @return director возвращается сущность режиссера
     */
    Director getDirectorById(Integer id);

    /**
     * Получение списка режиссеров по идентификатору фильма
     *
     * @param filmId идентификатор фильма у которого нужно получить режиссеров
     * @return возвращает список режиссеров этого фильма
     */
    Set<Director> getDirectorsByFilm(Long filmId);

    /**
     * Метод удаления сущности режиссера по идентификатору
     *
     * @param id идентификатор сущности
     */
    void deleteDirectorById(Integer id);

    /**
     * Метод для удаления списка режиссеров у фильма
     *
     * @param filmId фильм, у которого удаляется список режиссеров
     */
    void deleteDirectorsFromFilm(Long filmId);

    /**
     * Метод получения списка всех сущностей режиссеров из БД
     *
     * @return list возвращается список режиссеров
     */
    List<Director> getDirectors();

    /**
     * Метод для поиска режиссера в базе данных по идентификатору
     *
     * @param id идентификатор режиссера
     * @return возвращает true если режиссер есть в базу данных и false - если нет
     * @catch отлавливает ошибку EmptyResultDataAccessException.class
     */
    boolean isContains(Integer id);
}
