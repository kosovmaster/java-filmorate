package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest extends UserController {

    private static Validator validator;
    private static UserController userController;

    @BeforeAll
    static void beforeAll() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        userController = new UserController();
    }

    @Test
    public void testAddUser() {
        User user = User.builder()
                .id(1)
                .email("ivanov@mail.com")
                .login("ivanov333")
                .name("Ivan Ivanov")
                .birthday(LocalDate.of(2000, 1, 4))
                .build();
        User addedUser = userController.createUser(user).getBody();
        assertEquals(user, addedUser);
    }

    @Test
    public void testUpdateUser() {
        User existingUser = User.builder()
                .id(1)
                .email("ivanov.ivan@mail.com")
                .login("ivanov333")
                .name("Ivan Ivanov")
                .birthday(LocalDate.of(1990, 2, 5))
                .build();
        userController.createUser(existingUser);

        User updatedUser = User.builder()
                .id(existingUser.getId())
                .email("updated.ivanov@mail.com")
                .login("ivanov333")
                .name("Updated Ivanov")
                .birthday(LocalDate.of(1995, 8, 8))
                .build();
        User returnedUser = userController.updateUser(updatedUser).getBody();
        assertEquals(updatedUser, returnedUser);
    }

    @Test
    public void testAddUserWithInvalidEmail() {
        User user = User.builder()
                .id(1)
                .email("invalidEmail")
                .login("ivanov333")
                .name("Ivan Ivanov")
                .birthday(LocalDate.of(1990, 2, 5))
                .build();

        var violation = validator.validate(user);
        assertEquals(1, violation.size());
    }

    @Test

    public void testUpdateUserWithInvalidEmail() {
        User existingUser = User.builder()
                .id(1)
                .email("ivanov.ivan@mail.com")
                .login("ivanov333")
                .name("Ivan Ivanov")
                .birthday(LocalDate.of(1990, 2, 5))
                .build();
        userController.createUser(existingUser);

        User updatedUser = User.builder()
                .id(existingUser.getId())
                .email("updated.ivanov@mail.com")
                .login("updated.ivanov333")
                .name("Updated Ivanov")
                .birthday(LocalDate.of(1995, 8, 8))
                .build();
        updatedUser.setEmail("invalid.email");

        var violation = validator.validate(updatedUser);
        assertEquals(1, violation.size());
    }
}
