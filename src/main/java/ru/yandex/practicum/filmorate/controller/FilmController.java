package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@RestController
@Slf4j
public class FilmController {
    protected final HashMap<Integer, Film> films = new HashMap<>();
    protected Integer id = 1;

    @GetMapping("/films")
    public Collection<Film> getFilm() {
        return films.values();
    }

    @PostMapping("/films")
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        if (films.containsValue(film)) {
            throw new RuntimeException("Фильм с таким именем уже существует");
        }
        film.setId(id++);
        int filmId = film.getId();
        films.put(filmId, film);
        log.info("Фильм успешно добавлен: " + film.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(film);
    }

    @PutMapping("/films")
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film update) {
        if (!films.containsKey(update.getId())) {
            throw new RuntimeException("Фильм не найден");
        }
        if (films.containsValue(update) && !films.get(update.getId()).equals(update)) {
            throw new RuntimeException("Фильм с таким именем уже существует");
        }
        update.setName(update.getName());
        update.setDescription(update.getDescription());
        update.setReleaseDate(update.getReleaseDate());
        update.setDuration(update.getDuration());
        films.put(update.getId(), update);
        log.info("Фильм успешно обновлен: " + update.getName());
        return ResponseEntity.ok(update);
    }
}