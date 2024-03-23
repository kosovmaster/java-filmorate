package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest extends AbstractControllerTest {
    @Autowired
    private FilmController controller;
    @Autowired
    private FilmDbStorage filmStorage;
    @Autowired
    private MpaDbStorage mpaDbStorage;
    @Autowired
    private GenreDbStorage genreDbStorage;
    @Autowired
    private UserDbStorage userStorage;
    @Autowired
    private FilmService filmService;
    private Film testFilm;
    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @BeforeEach
    protected void init() {
        filmService = new FilmService(filmStorage, userStorage, genreDbStorage, mpaDbStorage);
        controller = new FilmController(filmService);
        testFilm = Film.builder()
                .id(1)
                .name("Тест название")
                .description("Описание фильма")
                .releaseDate(LocalDate.of(2000, 12, 12))
                .duration(190)
                .build();

        testFilm.setGenres(new HashSet<>());
        testFilm.setLikes(new HashSet<>());
        testFilm.setMpa(Mpa.builder()
                .id(1)
                .name("NC-17")
                .build());
    }

    @Test
    void createNewCorrectFilmIsOkTest() {
        testFilm.setId(null); // устанавливаем null для поля id
        Optional<Film> film = controller.addFilm(testFilm);
        assertEquals(film.get().getName(), testFilm.getName());
        assertEquals(film.get().getDescription(), testFilm.getDescription());
        assertEquals(film.get().getReleaseDate(), testFilm.getReleaseDate());
        assertEquals(film.get().getDuration(), testFilm.getDuration());
        assertEquals(film.get().getMpa(), testFilm.getMpa());
    }

    @Test
    void createFilmNameIsBlankBadRequestTest() {
        testFilm.setName("");
        try {
            controller.addFilm(testFilm);
        } catch (ValidationException e) {
            assertEquals("Некорректно указано название фильма", e.getMessage());
        }
    }

    @Test
    void createFilmRealiseDateInFutureBadRequestTest() {
        testFilm.setReleaseDate(LocalDate.of(2033, 4, 13));
        try {
            controller.addFilm(testFilm);
        } catch (ValidationException e) {
            assertEquals("Некорректно указана дата релиза.", e.getMessage());
        }
    }

    @Test
    void createFilmRealiseDateInPastBadRequestTest() {
        testFilm.setReleaseDate(LocalDate.of(1833, 4, 13));
        try {
            controller.addFilm(testFilm);
        } catch (ValidationException e) {
            assertEquals("Некорректно указана дата релиза.", e.getMessage());
        }
    }

    @Test
    void createFilm_IncorrectDescription_badRequestTest() {
        testFilm.setDescription("Размер описания значительно превышает двести символов. Размер описания значительно превышает двести символов." +
                "К сожалению размер описания фильма сейчас не превышает двести символов, размер описания значительно превышает двести символов." +
                "Сейчас однозначно стал превышать двести символов!");

        var violations = validator.validate(testFilm);
        assertEquals(1, violations.size());
    }
}
