package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {
    Collection<User> getUsers();

    ResponseEntity<User> createUser(@Valid @RequestBody User user);

    ResponseEntity<User> updateUser(@Valid @RequestBody User updatedUser);

    User deleteUser(Integer userId);

    Optional<User> findById(Integer userId) throws RuntimeException;

    Set<User> getUserFriends(Integer userId);

    Collection<User> getUserCrossFriends(Integer id, Integer otherId);
}
