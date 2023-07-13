package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.Validation;

import java.time.LocalDate;

@SpringBootTest
public class UserTest {
    Validation validation = new Validation();
    private final UserController userController = new UserController();

    private final User user = User.builder()
            .id(1)
            .email("ArkadyVolozh@yandex.ru")
            .login("ArkadyVolozh")
            .name("Arkady_Volozh")
            .birthday(LocalDate.of(1997, 9, 23))
            .build();

    @Test
    void emptyEmail() { // Если пустой email
        user.setEmail("");

        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(0, userController.getUser().size());
    }

    @Test
    void emailNotContainAnsi64() { // Если нет @
        user.setEmail("Lebedev.yandex.ru");

        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(0, userController.getUser().size());
    }

    @Test
    void loginEmpty() { // Если логин пуст
        user.setLogin("");

        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(0, userController.getUser().size());
    }

    @Test
    void loginContainSpace() { // Если логин содержит пробел
        user.setLogin("Yand ex");

        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(0, userController.getUser().size());
    }

    @Test
    void loginSpace() { // Если логин состоит из пробела
        user.setLogin(" ");

        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(0, userController.getUser().size());
    }

    @Test
    void emptyName() { // Если имя пустое, имя = логин
        user.setName(" ");
        validation.validationUser(user);

        Assertions.assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void dateOfBirthFromTheFuture() { // Если др позже чем завтра
    user.setBirthday(LocalDate.of(2136, 9, 23));

        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void dateOfBirthFromToDay() { // Если др сегодня, то все ок
        user.setBirthday(LocalDate.now());
        validation.validationUser(user);

        Assertions.assertEquals(user.getBirthday(), LocalDate.now());
    }
}