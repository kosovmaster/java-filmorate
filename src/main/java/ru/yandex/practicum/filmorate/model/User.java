package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Set<Integer> friends = new HashSet<>();
    @Min(value = 1, message = "Невозможно найти пользователя с данным ID")
    private Integer id;
    @Email(message = "Некорректно введён email. Электронная почта не может быть пустой и должна содержать символ @.")
    private String email;
    @NotEmpty(message = "Логин не может быть пустым и содержать пробелы")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;

    public void addFriend(User user) {
        this.friends.add(user.id);
    }
}
