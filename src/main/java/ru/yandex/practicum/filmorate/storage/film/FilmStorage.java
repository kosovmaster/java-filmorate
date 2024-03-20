package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.dao.FilmDao;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List<FilmDao> getFilm();

    Optional<FilmDao> addFilm(FilmDao film);

    FilmDao updateFilm(FilmDao updatedFilm) throws RuntimeException;

    void deleteFilm(Integer filmId);

    Optional<FilmDao> like(int filmId, int userId);

    Optional<FilmDao> deleteLike(int filmId, int userId);

    List<FilmDao> getMostPopular(Integer count);

    Optional<FilmDao> findById(Integer filmId);
}
