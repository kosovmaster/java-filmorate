package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage storage;
    private final UserServiceImpl service;

    @Override
    public Film addFilm(@Valid Film film) {
        Film newFilm = storage.addFilm(film);
        log.info("Фильм {} успешно добавлен", film.getName());
        return newFilm;
    }

    @Override
    public Collection<Film> getFilm() {
        return storage.getFilm();
    }

    @Override
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film updatedFilm) {
        Optional<Film> filmOptional = storage.findById(updatedFilm.getId());
        if (filmOptional.isEmpty()) {
            throw new NotFoundException("Фильм с id: " + updatedFilm.getId() + " не найден");
        }
        Film film = filmOptional.get();
        film.setName(updatedFilm.getName());
        film.setDescription(updatedFilm.getDescription());
        film.setReleaseDate(updatedFilm.getReleaseDate());
        film.setDuration(updatedFilm.getDuration());
        log.info("Фильм {} обновлён", updatedFilm.getName());
        return storage.updateFilm(film);
    }

    @Override
    public Optional<Film> findById(Integer filmId) {
        return storage.findById(filmId);
    }

    @Override
    public Film deleteFilm(Integer filmId) {
        return storage.deleteFilm(filmId);
    }

    @Override
    public List<Film> getMostPopular(Integer count) {
        return storage.getMostPopular(count);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        var film = storage.findById(filmId).orElseThrow(() -> new EntityNotFoundException("Фильм не найден с id: " + filmId));
        var user = service.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден с id: " + userId));

        film.addLike(userId);
        storage.updateFilm(film);
        log.info("Пользователь: {} поставил лайк фильму: {}", user, film);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        if (filmId < 1 || userId < 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти пользователя или фильм");
        }
        var film = storage.findById(filmId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден"));
        var user = service.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

        film.removeLike(userId);
        storage.updateFilm(film);
        log.info("Пользователь: {} удалил лайк фильму: {}", user, film);
    }
}
