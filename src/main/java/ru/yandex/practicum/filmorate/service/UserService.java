package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Optional<User> createUser(User user) {
        validationUser(user);
        UserDao userDao = fromUserToDao(user);
        Optional<UserDao> result = userStorage.createUser(userDao);
        if (result.isEmpty())
            return Optional.empty();
        return Optional.of(toMap(result.get()));
    }

    public List<User> getUsers() {
        var users = userStorage.getUsers();
        return users.stream()
                .map(this::toMap)
                .collect(Collectors.toList());
    }

    public Optional<User> findById(Integer userId) {
        var dao = userStorage.findById(userId);
        if (dao.isEmpty())
            return Optional.empty();
        return Optional.of(toMap(dao.get()));
    }

    public Collection<User> getFriends(Integer userId) {
        return userStorage
                .getUserFriends(userId)
                .stream()
                .map(this::toMap)
                .collect(Collectors.toList());
    }

    public List<User> getCrossFriends(int userId, int otherUserId) {
        return userStorage
                .getUserCrossFriends(userId, otherUserId)
                .stream()
                .map(this::toMap)
                .collect(Collectors.toList());
    }

    public void deleteUser(Integer userId) {
        userStorage.deleteUser(userId);
    }

    public Optional<User> updateUser(User updatedUser) {
        UserDao userDao = userStorage.updateUser(fromUserToDao(updatedUser));
        return Optional.of(toMap(userDao));
    }

    public Optional<User> addFriend(int userId, int friendId) {
        userStorage.addFriend(userId, friendId);
        var user = userStorage.findById(userId);
        if (user.isEmpty())
            return Optional.empty();
        return Optional.of(toMap(user.get()));
    }

    public Optional<User> removeFriend(int userId, int friendId) {
        userStorage.deleteFriend(userId, friendId);
        var user = userStorage.findById(userId);
        if (user.isEmpty())
            return Optional.empty();
        return Optional.of(toMap(user.get()));
    }

    private UserDao fromUserToDao(User user) {
        return new UserDao(
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getFriends());
    }

    private User toMap(UserDao userDao) {
        return User.builder()
                .id(userDao.id)
                .email(userDao.email)
                .login(userDao.login)
                .name(userDao.name)
                .birthday(userDao.birthday)
                .friends(userDao.friends)
                .build();
    }

    private void validationUser(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
