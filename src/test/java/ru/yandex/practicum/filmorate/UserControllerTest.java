package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserServiceImpl;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserControllerTest extends AbstractControllerTest {
    @Autowired
    private FilmController filmController;
    private static Validator validator;

    @Autowired
    UserController userController;

    @BeforeAll
    static void beforeAll() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @AfterEach
    void cleanData() {
        filmService.getFilm().forEach(film -> film.getLikes().clear());
        filmService.getFilm().clear();
        userServiceImpl = null;
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
                .login("updated.ivanov333")
                .name("updated Ivanov")
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

    @Test
    public void testAddFriendWithValidId() {
        UserServiceImpl service = Mockito.mock(UserServiceImpl.class);
        UserController controller = new UserController(service);
        controller.addFriend(1, 2);
        verify(service).addFriend(1, 2);
        verify(service).addFriend(2, 1);
    }

    @Test
    public void testRemoveFriendWithValidId() {
        UserServiceImpl service = Mockito.mock(UserServiceImpl.class);
        UserController controller = new UserController(service);
        controller.removeFriend(1, 2);
        verify(service).removeFriend(2, 1);
    }

    @Test
    public void testAddFriendWithInvalidId() {
        UserServiceImpl service = Mockito.mock(UserServiceImpl.class);
        UserController controller = new UserController(service);
        assertThrows(ResponseStatusException.class, () -> {
            controller.addFriend(0, 2);
        });
        verify(service, never()).addFriend(0, 2);
    }

    @Test
    public void testGetUsers() {
        UserServiceImpl service = Mockito.mock(UserServiceImpl.class);
        UserController controller = new UserController(service);
        User existingUser1 = User.builder()
                .id(1)
                .email("ivanov1.ivan@mail.com")
                .login("ivanov111")
                .name("Ivan1 Ivanov")
                .birthday(LocalDate.of(1990, 2, 5))
                .build();
        controller.createUser(existingUser1);
        User existingUser2 = User.builder()
                .id(2)
                .email("ivanov2.ivan@mail.com")
                .login("ivanov222")
                .name("Ivan2 Ivanov")
                .birthday(LocalDate.of(1991, 2, 5))
                .build();
        controller.createUser(existingUser2);
        User existingUser3 = User.builder()
                .id(3)
                .email("ivanov3.ivan@mail.com")
                .login("ivanov333")
                .name("Ivan3 Ivanov")
                .birthday(LocalDate.of(2000, 2, 5))
                .build();
        controller.createUser(existingUser3);
        when(service.getUsers()).thenReturn(Arrays.asList(
                existingUser1,
                existingUser2,
                existingUser3
        ));
        Collection<User> result = controller.getUsers();
        verify(service).getUsers();
        assertEquals(3, result.size());
    }

    @Test
    public void testGetFriends() {
        UserServiceImpl service = mock(UserServiceImpl.class);
        UserController controller = new UserController(service);
        User existingUser2 = User.builder()
                .id(2)
                .email("ivanov2.ivan@mail.com")
                .login("ivanov222")
                .name("Ivan2 Ivanov")
                .birthday(LocalDate.of(1980, 5, 15))
                .build();
        controller.createUser(existingUser2);
        User existingUser3 = User.builder()
                .id(3)
                .email("ivanov3.ivan@mail.com")
                .login("ivanov333")
                .name("Ivan3 Ivanov")
                .birthday(LocalDate.of(1995, 3, 20))
                .build();
        controller.createUser(existingUser3);
        when(service.getFriends(1)).thenReturn(Arrays.asList(
                existingUser2,
                existingUser3
        ));
        Collection<User> result = controller.getFriends(1);
        verify(service).getFriends(1);
        assertEquals(2, result.size());
        assertEquals(2, result.stream().map(User::getId).collect(Collectors.toList()).get(0));
        assertEquals(3, result.stream().map(User::getId).collect(Collectors.toList()).get(1));
    }

    @Test
    public void testGetCrossFriend() {
        UserServiceImpl service = mock(UserServiceImpl.class);
        UserController controller = new UserController(service);
        User existingUser1 = User.builder()
                .id(1)
                .email("ivanov1.ivan@mail.com")
                .login("ivanov111")
                .name("Ivan1 Ivanov")
                .birthday(LocalDate.of(1980, 5, 15))
                .build();
        controller.createUser(existingUser1);
        User existingUser2 = User.builder()
                .id(2)
                .email("ivanov2.ivan@mail.com")
                .login("ivanov222")
                .name("Ivan2 Ivanov")
                .birthday(LocalDate.of(1995, 3, 20))
                .build();
        controller.createUser(existingUser2);
        when(service.getCrossFriends(1, 2)).thenReturn(Arrays.asList(
                existingUser1,
                existingUser2
        ));
        Collection<User> result = controller.getCrossFriend(1, 2);
        verify(service).getCrossFriends(1, 2);
        assertEquals(2, result.size());
    }
}
