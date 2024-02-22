package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService implements FilmServiceInterface {
    @Autowired
    private final FilmStorage storage;
    private final UserService service;

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
        Optional<Film> filmOptional = storage.findById(filmId);
        Optional<User> userOptional = service.findById(userId);

        filmOptional.ifPresentOrElse(film -> {
            userOptional.ifPresentOrElse(user -> {
                film.addLike(userId);
                storage.updateFilm(film);
                log.info("Пользователь: {} поставил лайк фильму: {}", user, film);
            }, () -> log.warn("Пользователь с id: {} не найден", userId));
        }, () -> log.warn("Фильм с id: {} не найден", filmId));
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        Optional<Film> filmOptional = storage.findById(filmId);
        Optional<User> userOptional = service.findById(userId);

        filmOptional.ifPresentOrElse(film -> {
            userOptional.ifPresentOrElse(user -> {
                film.removeLike(userId);
                storage.updateFilm(film);
                log.info("Пользователь: {} поставил дислайк фильму: {}", user, film);
            }, () -> log.warn("Пользователь с id: {} не найден", userId));
        }, () -> log.warn("Фильм с id: {} не найден", filmId));
    }
}
