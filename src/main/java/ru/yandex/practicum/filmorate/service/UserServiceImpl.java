package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage storage;

    @Override
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        ResponseEntity<User> newUser = storage.createUser(user);
        log.info("Пользователь {} успешно добавлен.", user.getName());
        return newUser;
    }

    @Override
    public Collection<User> getUsers() {
        return storage.getUsers();
    }

    @Override
    public Optional<User> findById(Integer userId) {
        return storage.findById(userId);
    }

    @Override
    public Collection<User> getFriends(Integer userId) {
        return storage.getUserFriends(userId);
    }

    @Override
    public Collection<User> getCrossFriends(Integer userId, Integer otherUserId) {
        return storage.getUserCrossFriends(userId, otherUserId);
    }

    @Override
    public void deleteUser(Integer userId) {
        storage.deleteUser(userId);
    }

    @Override
    public ResponseEntity<User> updateUser(@Valid @RequestBody User updatedUser) {
        log.info("Обновлен пользователь: {}", updatedUser);
        return storage.updateUser(updatedUser);
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        storage.findById(friendId).ifPresent(friend -> {
            storage.findById(id).ifPresent(user -> {
                if (!user.getFriends().contains(friend.getId())) {
                    user.addFriend(friend);
                }
            });
        });
    }

    @Override
    public void removeFriend(Integer id, Integer userId) {
        storage.findById(id).ifPresent(user -> {
            user.getFriends().removeIf(friendId -> Objects.equals(friendId, userId));
        });
    }
}
