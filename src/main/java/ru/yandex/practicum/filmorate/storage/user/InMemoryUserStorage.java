package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.MissingOrBlankLoginException;
import ru.yandex.practicum.filmorate.exception.UnknownUserException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 1;

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Пользователь успешно добавлен: " + user.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Override
    public ResponseEntity<User> updateUser(@Valid @RequestBody User updatedUser) {
        if (!users.containsKey(updatedUser.getId())) {
            throw new UnknownUserException("Неизвестный пользователь");
        }
        if (updatedUser.getLogin() == null || updatedUser.getLogin().isBlank()) {
            throw new MissingOrBlankLoginException("Логин не может быть пустым");
        }
        if (updatedUser.getName() == null || updatedUser.getName().isBlank()) {
            updatedUser.setName(updatedUser.getLogin());
        }
        User user = users.get(updatedUser.getId());
        user.setName(updatedUser.getName());
        user.setLogin(updatedUser.getLogin());
        user.setEmail(updatedUser.getEmail());
        user.setBirthday(updatedUser.getBirthday());
        users.put(user.getId(), user);
        log.info("Пользователь успешно обновлён: " + updatedUser.getName());
        return ResponseEntity.ok(user);
    }

    @Override
    public User deleteUser(Integer userId) {
        User userToRemove = users.remove(userId);
        if (userToRemove == null) {
            log.debug("Ошибка удаления пользователя с ID: {}", userId);
            throw new UnknownUserException("Пользователь с ID: " + userId + " не найден");
        }
        log.debug("Удаление пользователя с ID: {}", userId);
        return userToRemove;
    }

    @Override
    public Optional<User> findById(Integer userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public Set<User> getUserFriends(Integer userId) {
        return users.get(userId)
                .getFriends()
                .stream().map(user -> findById(user).get())
                .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public Collection<User> getUserCrossFriends(Integer id, Integer otherId) {
        var userFriends = getUserFriends(id);
        var otherUserFriends = getUserFriends(otherId);

        return userFriends.stream().filter(otherUserFriends::contains).collect(Collectors.toCollection(HashSet::new));
    }
}
