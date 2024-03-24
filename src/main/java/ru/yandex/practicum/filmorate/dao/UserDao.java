package ru.yandex.practicum.filmorate.dao;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class UserDao {

    public Integer id;

    public String email;

    public String login;

    public String name;

    public LocalDate birthday;

    public Set<Integer> friends;

    public UserDao(Integer id, String email, String login, String name, LocalDate birthday, Set<Integer> friends) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = friends;
    }
}
