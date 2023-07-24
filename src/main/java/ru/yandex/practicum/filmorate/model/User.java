package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


/**
 * Класс-модель для создания пользователя со свойствами <b>id<b/>, <b>email<b/>, <b>login<b/>, <b>name<b/>, <b>birthday<b/>, <b>friends<b/>.
 */
@Data
public class User {
    /**
     * Поле идентификатор пользователя
     */
    private Long id;
    /**
     * Поле электронная почта пользователя
     */
    @Email
    @NotBlank
    @NotNull
    private String email;
    /**
     * Поле логин пользователя
     */
    @NotEmpty
    @NotBlank
    private String login;
    /**
     * Поле имя для отображения
     */
    private String name;
    /**
     * Поле даты рождения пользователя
     */
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
    /**
     * Поле со списком друзей пользователя
     */
    private Set<Long> friends = new HashSet<>();


    /**
     * Конструктор создание нового объекта пользователя.
     *
     * @see User#User(Long, String, String, String, LocalDate)
     */
    @Autowired
    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }


    /**
     * Метод добавление друга
     *
     * @param id id пользователя добавляемого в друзья.
     */
    public void addFriend(Long id) {
        friends.add(id);
    }

    /**
     * Метод удаление друга
     *
     * @param id id пользователя удаляемого из друзей.
     */
    public void deleteFriend(Long id) {
        friends.remove(id);
    }
}
