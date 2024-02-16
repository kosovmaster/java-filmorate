package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest extends FilmController {

    @Test
    public void testCreateFilm() {
        FilmController filmController = new FilmController();
        Film film = Film.builder()
                .id(1)
                .name("Titanic")
                .description("how woman at 92 years old dreaming about a young boy she knew for 2 week on a boat")
                .releaseDate(LocalDate.of(1998, 2, 20))
                .duration(195L)
                .build();
        Film addedFilm = filmController.addFilm(film).getBody();
        assertEquals(film, addedFilm);
    }

    @Test
    public void testUpdateFilm() {
        FilmController filmController = new FilmController();

        Film createdFilm = Film.builder()
                .id(1)
                .name("Titanic")
                .description("how woman at 92 years old dreaming about a young boy she knew for 2 week on a boat")
                .releaseDate(LocalDate.of(1998, 2, 20))
                .duration(195L)
                .build();
        filmController.addFilm(createdFilm);

        Film updateFilm = Film.builder()
                .id(createdFilm.getId())
                .name("Titanic")
                .description("how woman at 92 years old dreaming about a young boy she knew for 2 week on a boat, now with extra scenes")
                .releaseDate(LocalDate.of(1998, 2, 20))
                .duration(195L)
                .build();
        Film newFilm = filmController.updateFilm(updateFilm).getBody();
        assertEquals(updateFilm, newFilm);
    }
}
