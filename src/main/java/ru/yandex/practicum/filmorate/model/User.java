package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class User {

    private Long id; //идентификатор
    @Email
    @NotBlank
    @NotNull
    private String email; //электронная почта
    @NotEmpty
    @NotBlank
    private String login; //логин пользователя
    private String name; //имя для отображения
    @NotNull
    @PastOrPresent
    private LocalDate birthday; //дата рождения
    private Set<Long> friends = new HashSet<>(); //Список друзей

    @Autowired
    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFriend(Long id) {
        friends.add(id);
    }

    public void deleteFriend(Long id) {
        friends.remove(id);
    }
}
