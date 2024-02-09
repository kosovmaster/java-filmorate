package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private Integer id;
    @Email(message = "Некорректно введён email. Электронная почта не может быть пустой и должна содержать символ @.")
    private String email;
    @NotEmpty(message = "Логин не может быть пустым и содержать пробелы")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
