package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id; //идентификатор
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
}
