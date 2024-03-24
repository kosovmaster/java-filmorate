package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class FilmDao {

    public Integer id;

    public String name;

    public String description;

    public LocalDate releaseDate;

    public Long duration;

    public Mpa mpa;
}