package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaStorage mpaStorage;

    public Optional<Mpa> getMpaRating(int ratingMpaId) {
        return mpaStorage.getMpaForId(ratingMpaId);
    }

    public Collection<Mpa> findAll() {
        return mpaStorage.getAll();
    }
}
