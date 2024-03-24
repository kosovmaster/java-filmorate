package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public Collection<Genre> getAll() {
        log.info("Получение GET-запроса на получение списка всех жанров");
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Genre> getGenre(@PathVariable("id") int genreId) {
        log.info("Получение GET-запроса на получение жанра по id: " + genreId);
        return genreService.getGenre(genreId);
    }
}
