package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.dao.UserDao;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<UserDao> getUsers();

    Optional<UserDao> createUser(UserDao userDao);

    UserDao updateUser(UserDao updatedUser);

    void deleteUser(Integer userId);

    Optional<UserDao> findById(Integer userId) throws RuntimeException;

    Optional<UserDao> addFriend(Integer userId, Integer friendId);

    Collection<UserDao> getUserFriends(Integer userId);

    List<UserDao> getUserCrossFriends(Integer id, Integer otherId);

    Optional<UserDao> deleteFriend(Integer userId, Integer friendId);
}
