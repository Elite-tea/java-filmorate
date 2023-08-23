//package ru.yandex.practicum.filmorate;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import ru.yandex.practicum.filmorate.Exception.ValidationException;
//import ru.yandex.practicum.filmorate.model.User;
//import ru.yandex.practicum.filmorate.service.UserService;
//import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
//import ru.yandex.practicum.filmorate.storage.user.UserStorage;
//import ru.yandex.practicum.filmorate.validation.Validation;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@SpringBootTest
//public class UserTest {
//    private final UserStorage inMemoryUserStorage = new InMemoryUserStorage();
//    private final UserService userService = new UserService(inMemoryUserStorage);
//
//
//    private final User user = new User(1L, "ArkadyVolozh@yandex.ru", "ArkadyVolozh", "Arkady_Volozh", LocalDate.of(1997, 9, 23));
//
//    private final User user2 = new User(2L, "ArkadyVolozh2@yandex.ru", "ArkadyVolozh", "Arkady__Volozh", LocalDate.of(1997, 9, 23));
//
//    private final User user3 = new User(3L, "ArkadyVolozh3@yandex.ru", "ArkadyVolozh", "Arkady___Volozh", LocalDate.of(1997, 9, 23));
//
//
//    @Test
//    void emptyEmail() { // Если пустой email
//        user.setEmail("");
//
//        Assertions.assertThrows(ValidationException.class, () -> inMemoryUserStorage.create(user));
//        Assertions.assertEquals(0, inMemoryUserStorage.getUser().size());
//    }
//
//    @Test
//    void emailNotContainAnsi64() { // Если нет @
//        user.setEmail("Lebedev.yandex.ru");
//
//        Assertions.assertThrows(ValidationException.class, () -> inMemoryUserStorage.create(user));
//        Assertions.assertEquals(0, inMemoryUserStorage.getUser().size());
//    }
//
//    @Test
//    void loginEmpty() { // Если логин пуст
//        user.setLogin("");
//
//        Assertions.assertThrows(ValidationException.class, () -> inMemoryUserStorage.create(user));
//        Assertions.assertEquals(0, inMemoryUserStorage.getUser().size());
//    }
//
//    @Test
//    void loginContainSpace() { // Если логин содержит пробел
//        user.setLogin("And ex");
//
//        Assertions.assertThrows(ValidationException.class, () -> inMemoryUserStorage.create(user));
//        Assertions.assertEquals(0, inMemoryUserStorage.getUser().size());
//    }
//
//    @Test
//    void loginSpace() { // Если логин состоит из пробела
//        user.setLogin(" ");
//
//        Assertions.assertThrows(ValidationException.class, () -> inMemoryUserStorage.create(user));
//        Assertions.assertEquals(0, inMemoryUserStorage.getUser().size());
//    }
//
//    @Test
//    void emptyName() { // Если имя пустое, имя = логин
//        user.setName(" ");
//        Validation.validationUser(user);
//
//        Assertions.assertEquals(user.getName(), user.getLogin());
//    }
//
//    @Test
//    void dateOfBirthFromTheFuture() { // Если др позже чем завтра
//        user.setBirthday(LocalDate.of(2136, 9, 23));
//
//        Assertions.assertThrows(ValidationException.class, () -> inMemoryUserStorage.create(user));
//    }
//
//    @Test
//    void dateOfBirthFromToDay() { // Если др сегодня, то все ок
//        user.setBirthday(LocalDate.now());
//        Validation.validationUser(user);
//
//        Assertions.assertEquals(user.getBirthday(), LocalDate.now());
//    }
//
//    @Test
//    void addFriend() { // добавить друга
//        Set<Long> friendsTest = new HashSet<>();
//        friendsTest.add(2L);
//        user.addFriend(2L);
//
//        Assertions.assertEquals(user.getFriends(), friendsTest);
//    }
//
//    @Test
//    void delFriend() { // удалить друга
//        Set<Long> friendsTest = new HashSet<>();
//        user.addFriend(2L);
//        user.deleteFriend(2L);
//        Assertions.assertEquals(user.getFriends(), friendsTest);
//    }
//
//    @Test
//    void getFriends() { // вывести список общих друзей
//        List<User> friendsTest = new ArrayList<>();
//        friendsTest.add(user3);
//        user.addFriend(3L);
//        user.addFriend(4L);
//        user2.addFriend(3L);
//        inMemoryUserStorage.create(user);
//        inMemoryUserStorage.create(user2);
//        inMemoryUserStorage.create(user3);
//
//        Assertions.assertEquals(userService.getMutualFriends(user.getId(), user2.getId()), friendsTest);
//    }
//
//    @Test
//    void getUserId() { // Запрос пользователя по id
//        inMemoryUserStorage.create(user);
//
//        Assertions.assertEquals(inMemoryUserStorage.getByIdUser(1L), user);
//    }
//}