package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    public Collection<Mpa> findAll() {
        log.info("Получен GET-запроса на получение списка MPA");
        return mpaService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Mpa> getMpaRating(@PathVariable("id") int mpaId) {
        log.info("Получение GET-запроса на получение MPA по id: " + mpaId);
        return mpaService.getMpaRating(mpaId);
    }
}