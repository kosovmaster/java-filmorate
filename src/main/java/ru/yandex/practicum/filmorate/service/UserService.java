package ru.yandex.practicum.filmorate.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

public interface UserService {
    ResponseEntity<User> createUser(@Valid @RequestBody User user);

    Collection<User> getUsers();

    Optional<User> findById(Integer userId);

    Collection<User> getFriends(Integer userId);

    Collection<User> getCrossFriends(Integer userId, Integer otherUserId);

    void deleteUser(Integer userId);

    ResponseEntity<User> updateUser(@Valid @RequestBody User updatedUser);

    void addFriend(Integer id, Integer friendId);

    void removeFriend(Integer id, Integer userId);
}
