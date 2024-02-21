package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getFilm();

    ResponseEntity<Film> addFilm(@Valid @RequestBody Film film);

    ResponseEntity<Film> updateFilm(@Valid @RequestBody Film updatedFilm);

    Film deleteFilm(Integer filmId);

    List<Film> getMostPopular(Integer count);

    Optional<Film> findById(Integer filmId);
}
