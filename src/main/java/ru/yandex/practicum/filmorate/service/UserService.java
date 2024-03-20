package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ValidationException;
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
        return userStorage.createUser(userDao).map(this::toMap);
    }

    public List<User> getUsers() {
        return userStorage.getUsers().stream()
                .map(this::toMap)
                .collect(Collectors.toList());
    }

    public Optional<User> findById(Integer userId) {
        var dao = userStorage.findById(userId);
        if (dao.isEmpty()) {
            return Optional.empty();
        }
        return dao.map(this::toMap);
    }

    public Collection<User> getFriends(Integer userId) {
        return userStorage.getUserFriends(userId).stream()
                .map(this::toMap)
                .collect(Collectors.toList());
    }

    public List<User> getCrossFriends(int userId, int otherUserId) {
        return userStorage.getUserCrossFriends(userId, otherUserId).stream()
                .map(this::toMap)
                .collect(Collectors.toList());
    }

    public void deleteUser(Integer userId) {
        userStorage.deleteUser(userId);
    }

    public Optional<User> updateUser(User updatedUser) {
        UserDao userDao = userStorage.updateUser(fromUserToDao(updatedUser));
        return Optional.ofNullable(toMap(userDao));
    }

    public Optional<User> addFriend(int userId, int friendId) {
        userStorage.addFriend(userId, friendId);
        var user = userStorage.findById(userId);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(toMap(user.get()));
    }

    public Optional<User> removeFriend(int userId, int friendId) {
        userStorage.deleteFriend(userId, friendId);
        var user = userStorage.findById(userId);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(toMap(user.get()));
    }

    private UserDao fromUserToDao(User user) {
        UserDao userDao = new UserDao(
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getFriends());
        return userDao;
    }

    private User toMap(UserDao userDao) {
        User user = User.builder()
                .id(userDao.id)
                .email(userDao.email)
                .login(userDao.login)
                .name(userDao.name)
                .birthday(userDao.birthday)
                .friends(userDao.friends)
                .build();

        return user;
    }

    private void validationUser(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
