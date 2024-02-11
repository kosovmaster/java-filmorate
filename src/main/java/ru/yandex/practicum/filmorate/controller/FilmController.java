package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@RestController
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private Integer id = 1;

    @GetMapping("/films")
    public Collection<Film> getFilm() {
        return films.values();
    }

    @PostMapping("/films")
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        if (films.containsValue(film)) {
            throw new FilmAlreadyExistsException("Фильм с таким именем уже существует");
        }
        film.setId(id++);
        int filmId = film.getId();
        films.put(filmId, film);
        log.info("Фильм успешно добавлен: " + film.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(film);
    }

    @PutMapping("/films")
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film updatedFilm) {
        Film film = films.get(updatedFilm.getId());
        if (film == null) {
            throw new FilmNotFoundException("Фильм не найден");
        }
        if (films.containsValue(updatedFilm) && !film.equals(updatedFilm)) {
            throw new FilmAlreadyExistsException("Фильм с таким именем уже существует");
        }
        film.setName(updatedFilm.getName());
        film.setDescription(updatedFilm.getDescription());
        film.setReleaseDate(updatedFilm.getReleaseDate());
        film.setDuration(updatedFilm.getDuration());
        films.put(film.getId(), film);
        log.info("Фильм успешно обновлен: " + updatedFilm.getName());
        return ResponseEntity.ok(film);
    }
}