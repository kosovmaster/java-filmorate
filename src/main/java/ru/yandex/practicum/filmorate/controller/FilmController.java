package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmServiceImpl service;

    @GetMapping
    public Collection<Film> getFilms() {
        return service.getFilm();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return service.addFilm(film);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film updatedFilm) {
        return service.updateFilm(updatedFilm);
    }

    @DeleteMapping("{filmId}")
    public void deleteFilm(@Valid @PathVariable Integer filmId) {
        service.deleteFilm(filmId);
    }

    @GetMapping("{id}")
    public ResponseEntity<Film> getFilm(@Valid @PathVariable Integer id) {
        return service.findById(id)
                .map(film -> ResponseEntity.ok().body(film))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Невозможно найти фильм с указанным ID"));
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@Valid @PathVariable Integer id, @Valid @PathVariable Integer userId) {
        service.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLike(@Valid @PathVariable Integer id, @Valid @PathVariable Integer userId) {
        service.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@Valid @RequestParam(defaultValue = "10", name = "count") Integer count) {
        return service.getMostPopular(count);
    }
}