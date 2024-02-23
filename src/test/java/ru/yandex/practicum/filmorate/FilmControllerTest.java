package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FilmControllerTest extends AbstractControllerTest {
    @Autowired
    private FilmController filmController;

    @Test
    public void testCreateFilm() {
        Film film = Film.builder()
                .id(1)
                .name("Titanic")
                .description("how woman at 92 years old dreaming about a young boy she knew for 2 week on a boat")
                .releaseDate(LocalDate.of(1998, 2, 20))
                .duration(195L)
                .build();
        Film addedFilm = filmController.addFilm(film);
        assertEquals(film, addedFilm);
    }

    @Test
    public void testUpdateFilm() {
        Film createdFilm = Film.builder()
                .id(2)
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

    @Test
    public void testDeleteFilm() {
        FilmServiceImpl service = Mockito.mock(FilmServiceImpl.class);
        FilmController controller = new FilmController(service);
        controller.deleteFilm(1);
        Mockito.verify(service, Mockito.times(1)).deleteFilm(1);
    }

    @Test
    public void testGetFilmWithValidId() {
        Film createdFilm3 = Film.builder()
                .id(3)
                .name("Фильм3")
                .description("Описание3")
                .releaseDate(LocalDate.of(2000, 2, 20))
                .duration(150L)
                .build();
        FilmServiceImpl service = Mockito.mock(FilmServiceImpl.class);
        FilmController controller = new FilmController(service);
        when(service.findById(1)).thenReturn(Optional.of(createdFilm3));
        ResponseEntity<Film> result = controller.getFilm(1);
        verify(service).findById(1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Фильм3", result.getBody().getName());
        assertEquals("Описание3", result.getBody().getDescription());
    }

    @Test
    public void testGetFilmWithInvalidId() {
        FilmServiceImpl service = Mockito.mock(FilmServiceImpl.class);
        FilmController controller = new FilmController(service);
        when(service.findById(10)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> {
            controller.getFilm(10);
        });
        verify(service).findById(10);
    }

    @Test
    public void testGetPopular() {
        FilmServiceImpl service = Mockito.mock(FilmServiceImpl.class);
        FilmController controller = new FilmController(service);
        Film createdFilm1 = Film.builder()
                .id(1)
                .name("Фильм1")
                .description("Описание1")
                .releaseDate(LocalDate.of(2000, 2, 20))
                .duration(150L)
                .build();
        controller.addFilm(createdFilm1);
        Film createdFilm2 = Film.builder()
                .id(2)
                .name("Фильм2")
                .description("Описание2")
                .releaseDate(LocalDate.of(2000, 2, 20))
                .duration(150L)
                .build();
        controller.addFilm(createdFilm2);
        Film createdFilm3 = Film.builder()
                .id(3)
                .name("Фильм3")
                .description("Описание3")
                .releaseDate(LocalDate.of(1998, 2, 20))
                .duration(150L)
                .build();
        controller.addFilm(createdFilm3);
        Film createdFilm4 = Film.builder()
                .id(4)
                .name("Фильм4")
                .description("Описание4")
                .releaseDate(LocalDate.of(1995, 2, 20))
                .duration(150L)
                .build();
        controller.addFilm(createdFilm4);
        Film createdFilm5 = Film.builder()
                .id(5)
                .name("Фильм5")
                .description("Описание5")
                .releaseDate(LocalDate.of(2005, 2, 20))
                .duration(150L)
                .build();
        controller.addFilm(createdFilm5);

        when(service.getMostPopular(5)).thenReturn(Arrays.asList(
                createdFilm1,
                createdFilm2,
                createdFilm3,
                createdFilm4,
                createdFilm5
        ));
        Collection<Film> result = controller.getPopular(5);
        verify(service).getMostPopular(5);
        assertEquals(5, result.size());
    }

    @Test
    public void testAddLike() {
        FilmServiceImpl service = Mockito.mock(FilmServiceImpl.class);
        FilmController controller = new FilmController(service);
        controller.addLike(1, 232);
        verify(service).addLike(1, 232);
    }

    @Test
    public void testRemoveLike() {
        FilmServiceImpl service = Mockito.mock(FilmServiceImpl.class);
        FilmController controller = new FilmController(service);
        controller.removeLike(1, 232);
        verify(service).removeLike(1, 232);
    }

    @Test
    public void testGetFilms() {
        FilmServiceImpl service = Mockito.mock(FilmServiceImpl.class);
        FilmController controller = new FilmController(service);
        Film createdFilm1 = Film.builder()
                .id(1)
                .name("Фильм1")
                .description("Описание1")
                .releaseDate(LocalDate.of(2000, 2, 20))
                .duration(150L)
                .build();
        controller.addFilm(createdFilm1);
        Film createdFilm2 = Film.builder()
                .id(2)
                .name("Фильм2")
                .description("Описание2")
                .releaseDate(LocalDate.of(2000, 2, 20))
                .duration(150L)
                .build();
        controller.addFilm(createdFilm2);
        Film createdFilm3 = Film.builder()
                .id(3)
                .name("Фильм3")
                .description("Описание3")
                .releaseDate(LocalDate.of(1998, 2, 20))
                .duration(150L)
                .build();
        controller.addFilm(createdFilm3);
        when(service.getFilm()).thenReturn(Arrays.asList(
                createdFilm1,
                createdFilm2,
                createdFilm3
        ));
        Collection<Film> result = controller.getFilms();
        verify(service).getFilm();
        assertEquals(3, result.size());
    }
}
