package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Data
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private Integer id = 1;

    @Override
    public Collection<Film> getFilm() {
        return films.values();
    }

    @Override
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

    @Override
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

    @Override
    public Film deleteFilm(Integer filmId) {
        Film filmToRemove = films.remove(filmId);
        if (filmToRemove == null) {
            log.debug("Ошибка удаления фильма с ID: {}", filmId);
            throw new FilmNotFoundException("Фильм с ID: " + filmId + " не найден");
        }
        log.debug("Удаление фильма с ID: {}", filmId);
        return filmToRemove;
    }

    @Override
    public List<Film> getMostPopular(Integer count) {
        return films.values().stream().sorted(Comparator.comparingInt(f -> -f.getLikes().size())).limit(count).collect(Collectors.toList());
    }

    @Override
    public Optional<Film> findById(Integer filmId) {
        if (films.containsKey(filmId)) {
            return Optional.of(films.get(filmId));
        } else {
            return Optional.empty();
        }
    }
}
