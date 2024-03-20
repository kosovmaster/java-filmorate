package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validator.IsAfter;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Collection;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    @NotNull
    private Mpa mpa;
    private Collection<Genre> genres;
    private Collection<Integer> likes;
    @Min(value = 1, message = "Невозможно найти фильм с данным ID")
    protected Integer id;
    @NotEmpty(message = "Название фильма не может быть пустым.")
    private String name;
    @Size(max = 200)
    private String description;
    @IsAfter(current = "1895-12-28", message = "Ошибка поиска. Дата релиза должна быть позже 28 декабря 1895 года.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Positive
    private long duration;
}
