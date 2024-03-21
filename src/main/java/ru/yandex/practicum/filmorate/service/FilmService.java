package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;

    public Optional<Film> createFilm(Film film) {
        FilmDao filmDao = map(film);
        Optional<FilmDao> result = filmStorage.addFilm(filmDao);
        film.setId(result.get().getId());
        mpaDbStorage.addMpaToFilm(film);
        genreDbStorage.addGenreNameToFilm(film);
        genreDbStorage.addGenresForCurrentFilm(film);
        if (result.isEmpty())
            return Optional.empty();
        return Optional.of(map(result.get()));
    }

    public Collection<Film> getFilm() {
        return filmStorage
                .getFilm()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public Film updateFilm(Film updatedFilm) {
        Optional<FilmDao> filmOptional = filmStorage.findById(updatedFilm.getId());
        if (filmOptional.isEmpty()) {
            throw new NotFoundException("Фильм с id: " + updatedFilm.getId() + " не найден");
        }
        filmStorage.updateFilm(map(updatedFilm));
        mpaDbStorage.addMpaToFilm(updatedFilm);
        genreDbStorage.updateGenresForCurrentFilm(updatedFilm);
        genreDbStorage.addGenreNameToFilm(updatedFilm);
        updatedFilm.setGenres(genreDbStorage.getGenreForCurrentFilm(updatedFilm.getId()));
        return updatedFilm;
    }


    public Optional<Film> findById(Integer filmId) {
        var result = filmStorage.findById(filmId);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(map(result.get()));
    }

    public void deleteFilm(Integer filmId) {
        filmStorage.deleteFilm(filmId);
    }

    public List<Film> getMostPopular(Integer count) {
        return filmStorage
                .getMostPopular(count)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public Optional<Film> addLike(int filmId, int userId) {
        if (userStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        var result = filmStorage.like(filmId, userId);
        if (result.isEmpty())
            return Optional.empty();
        return Optional.of(map(result.get()));
    }

    public Optional<Film> removeLike(int filmId, int userId) {
        if (userStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        var result = filmStorage.deleteLike(filmId, userId);
        if (result.isEmpty())
            return Optional.empty();
        return Optional.of(map(result.get()));
    }

    public static FilmDao map(Film film) {
        return FilmDao.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .mpaId(film.getMpa().getId())
                .build();
    }

    private Film map(FilmDao filmDao) {
        var mpa = mpaDbStorage.getMpaForId(filmDao.mpaId);
        var genres = genreDbStorage.getGenreForCurrentFilm(filmDao.id);
        Film film = Film.builder()
                .id(filmDao.getId())
                .name(filmDao.getName())
                .description(filmDao.getDescription())
                .releaseDate(filmDao.getReleaseDate())
                .duration(filmDao.getDuration())
                .mpa(mpa.get())
                .genres(genres)
                .build();
        validate(film);
        return film;
    }

    public void validate(Film film) throws ValidationException {

    }
}
