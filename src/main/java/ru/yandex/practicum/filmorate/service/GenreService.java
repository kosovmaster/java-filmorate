package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {
    private  final GenreStorage genreStorage;

    public Optional<Genre> getGenre(int genreId) {
        return genreStorage.getGenreForId(genreId);
    }

    public Collection<Genre> findAll() {
        return genreStorage.getAll();
    }
}
