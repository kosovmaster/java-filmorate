package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.UnknownUserException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 1;

    @GetMapping("/users")
    public ResponseEntity<Collection<User>> getUsers() {
        return ResponseEntity.ok(users.values());
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Пользователь успешно добавлен: " + user.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User updatedUser) {
        User user = users.get(updatedUser.getId());
        if (!users.containsKey(updatedUser.getId())) {
            throw new UnknownUserException("Неизвестный пользователь");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(updatedUser.getLogin());
        }
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setBirthday(updatedUser.getBirthday());
        users.put(user.getId(), user);
        log.info("Пользователь успешно обновлён: " + updatedUser.getName());
        return ResponseEntity.ok(user);
    }
}