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
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Поступил запрос на получение списка всех фильмов.");
        return filmService.getFilm();
    }

    @PostMapping
    public Optional<Film> addFilm(@Valid @RequestBody Film film) {
        log.info("Поступил запрос на добавление фильма.");
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film updatedFilm) {
        log.info("Поступил запрос на изменения фильма.");
        return filmService.updateFilm(updatedFilm);
    }

    @DeleteMapping("{filmId}")
    public void deleteFilm(@Valid @PathVariable Integer filmId) {
        log.info("Поступил запрос на удаление фильма.");
        filmService.deleteFilm(filmId);
    }

    @GetMapping("{id}")
    public ResponseEntity<Film> getFilm(@Valid @PathVariable Integer id) {
        log.info("Получен GET-запрос на получение фильма");
        return filmService.findById(id)
                .map(film -> ResponseEntity.ok().body(film))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Невозможно найти фильм с указанным ID"));
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@Valid @PathVariable Integer id, @Valid @PathVariable Integer userId) {
        log.info("Поступил запрос на присвоение лайка фильму.");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLike(@Valid @PathVariable Integer id, @Valid @PathVariable Integer userId) {
        log.info("Поступил запрос на удаление лайка у фильма.");
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@Valid @RequestParam(defaultValue = "10", name = "count") Integer count) {
        log.info("Поступил запрос на получение списка популярных фильмов.");
        return filmService.getMostPopular(count);
    }
}