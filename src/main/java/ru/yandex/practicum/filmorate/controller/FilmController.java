package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getFilm();
    }

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film updatedFilm) {
        return filmService.updateFilm(updatedFilm);
    }

    @DeleteMapping("{filmId}")
    public void deleteFilm(@Valid @PathVariable Integer filmId) {
        filmService.deleteFilm(filmId);
    }

    @GetMapping("{id}")
    public Optional<Film> getFilm(@Valid @RequestBody Integer id) {
        var film = filmService.findById(id);
        if (film.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Невозможно найти фильм с указанным ID");
        }
        return film;
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@Valid @PathVariable Integer id, @Valid @PathVariable Integer userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLike(@Valid @PathVariable Integer id, @Valid @PathVariable Integer userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@Valid @RequestParam(defaultValue = "10", name = "count") Integer count) {
        return filmService.getMostPopular(count);
    }
}