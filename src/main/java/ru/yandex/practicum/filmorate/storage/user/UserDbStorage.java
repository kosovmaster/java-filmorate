package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Primary
@Slf4j
@Component
@RequiredArgsConstructor
@Qualifier
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<UserDao> getUsers() {
        List<UserDao> users = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM users");
        while (rowSet.next()) {
            try {
                UserDao userDao = mapSqlRowSetToUser(rowSet, rowSet.getRow());
                users.add(userDao);
            } catch (Exception e) {
                log.error("Ошибка {}", e.getMessage(), e);
            }
        }
        return users;
    }

    @Override
    public Optional<UserDao> createUser(UserDao userDao) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("user_id");
        userDao.id = simpleJdbcInsert.executeAndReturnKey(toMap(userDao)).intValue();
        log.info("Пользователь добавлен в базу данных");
        return Optional.of(userDao);
    }

    @Override
    public UserDao updateUser(UserDao updatedUser) {
        String sqlQuery = "UPDATE USERS SET " +
                "email=?, login=?, name=?, birthday=? WHERE user_id=?";
        int rowCount = jdbcTemplate.update(sqlQuery, updatedUser.getEmail(), updatedUser.getLogin(),
                updatedUser.getName(), updatedUser.getBirthday(), updatedUser.getId());
        if (rowCount > 0) {
            log.info("Пользователь изменён");
            return updatedUser;
        }
        throw new NotFoundException("Пользователь не найден");
    }

    @Override
    public void deleteUser(Integer userId) {
        Optional<UserDao> userDao = findById(userId);
        if (userDao.isPresent()) {
            String sqlQuery =
                    "DELETE " +
                            "FROM users " +
                            "WHERE user_id = ?";
            jdbcTemplate.update(sqlQuery, userId);
            log.info("Пользователь удалён");
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public Optional<UserDao> findById(Integer userId) throws RuntimeException {
        String sqlQuery = "SELECT user_id, email, login, name, birthday FROM users WHERE user_id=?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId));
        } catch (RuntimeException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public Optional<UserDao> addFriend(Integer userId, Integer friendId) {
        Optional<UserDao> userDao = findById(userId);
        try {
            findById(friendId);
        } catch (RuntimeException e) {
            throw new NotFoundException("Пользователь не найден.");
        }
        String sqlQuery = "INSERT INTO friends (user_id, friend_id) VALUES(?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return userDao;
    }

    @Override
    public Optional<UserDao> deleteFriend(Integer userId, Integer friendId) {
        Optional<UserDao> userDao = findById(userId);
        String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return userDao;
    }

    @Override
    public Collection<UserDao> getUserFriends(Integer userId) {
        final String sql = "SELECT * From USERS where USER_ID IN (SELECT FRIEND_ID FROM FRIENDS where USER_ID = ?)";
        Collection<UserDao> friends = new HashSet<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        while (rowSet.next()) {
            try {
                UserDao userDao = mapSqlRowSetToUser(rowSet, rowSet.getRow());
                friends.add(userDao);
            } catch (Exception exception) {
                log.error("Unhandled exception {}", exception.getMessage(), exception);
            }
        }
        return friends.stream()
                .sorted(Comparator.comparing(UserDao::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDao> getUserCrossFriends(Integer id, Integer otherId) {
        String sqlQuery = "SELECT user_id, email, login, name, birthday FROM users WHERE user_id IN(" +
                "SELECT friend_id FROM friends WHERE user_id = ?) " +
                "AND user_id IN(SELECT friend_id FROM friends WHERE user_id = ?)";
        return new ArrayList<>(jdbcTemplate.query(sqlQuery, this::mapRowToUser, id, otherId));
    }

    private UserDao mapSqlRowSetToUser(SqlRowSet rowSet, int rowNum) throws SQLException {
        UserDao userDao = UserDao.builder()
                .id(rowSet.getInt("user_id"))
                .email(rowSet.getString("email"))
                .login(rowSet.getString("login"))
                .name(rowSet.getString("name"))
                .birthday(rowSet.getDate("birthday").toLocalDate())
                .build();
        userDao.setFriends(getUserFriends(userDao.getId()).stream()
                .map(UserDao::getId)
                .collect(Collectors.toSet()));
        return userDao;
    }

    private UserDao mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        UserDao userDao = UserDao.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
        userDao.setFriends(getUserFriends(userDao.getId()).stream()
                .map(UserDao::getId)
                .collect(Collectors.toSet()));
        return userDao;
    }

    private Map<String, Object> toMap(UserDao user) {
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.email);
        values.put("login", user.login);
        values.put("name", user.name);
        values.put("birthday", user.birthday);
        return values;
    }
}
