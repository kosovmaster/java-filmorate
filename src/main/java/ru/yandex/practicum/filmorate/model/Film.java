package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validator.IsAfter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    private Integer id;
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
