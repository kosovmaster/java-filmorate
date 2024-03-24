package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreStorage {
    Collection<Genre> getAll();

    Optional<Genre> getGenreForId(int genreId);

    void addGenresForCurrentFilm(Film film);

    void updateGenresForCurrentFilm(Film film);

    Collection<Genre> getGenreForCurrentFilm(int filmId);
}
