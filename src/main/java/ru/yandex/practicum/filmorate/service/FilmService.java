package ru.yandex.practicum.filmorate.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmService {

    Film addFilm(@Valid Film film);

    Collection<Film> getFilm();

    ResponseEntity<Film> updateFilm(@Valid @RequestBody Film updatedFilm);

    Optional<Film> findById(Integer filmId);

    Film deleteFilm(Integer filmId);

    List<Film> getMostPopular(Integer count);

    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);
}
