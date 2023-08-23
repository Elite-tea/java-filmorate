package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.*;
import java.time.LocalDate;


/**
 * Класс-модель для создания пользователя со свойствами <b>id<b/>, <b>email<b/>, <b>login<b/>, <b>name<b/>, <b>birthday<b/>, <b>friends<b/>.
 */
@Data
@NoArgsConstructor
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
     * Конструктор создание нового объекта пользователя.
     *
     * @see User#User(String, String, String, LocalDate)
     */
    @Autowired
    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}