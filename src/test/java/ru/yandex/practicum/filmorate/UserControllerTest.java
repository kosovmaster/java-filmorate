package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest extends AbstractControllerTest {
    UserController userController;
    @Autowired
    UserDbStorage userDbStorage;
    UserService userService;
    User testUser;

    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @BeforeEach
    protected void init() {
        userService = new UserService(userDbStorage);
        userController = new UserController(userService);

        testUser = User.builder()
                .name("Ivan")
                .email("ivan@mair.com")
                .login("login")
                .birthday(LocalDate.of(1988, 2, 3))
                .build();
    }

    @Test
    public void createUserNameIsBlankNameIsLoginTest() {
        testUser.setName("");
        userController.createUser(testUser);
        assertEquals("login", userController.getUsers().get(0).getName());
    }

    @Test
    void createUserBirthdayInFutureBadRequestTest() {
        testUser.setBirthday(LocalDate.parse("2024-12-12"));
        var violations = validator.validate(testUser);
        assertEquals(1, violations.size());
    }
}
