package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
@Qualifier
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> getAll() {
        Collection<Genre> genreList = new ArrayList<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT genre_id, name FROM GENRES");
        while (genreRows.next()) {
            Genre genre = Genre.builder()
                    .id(genreRows.getInt("genre_id"))
                    .name(genreRows.getString("name"))
                    .build();
            genreList.add(genre);
        }
        return genreList;
    }

    @Override
    public Optional<Genre> getGenreForId(int genreId) {
        SqlRowSet sqlQuery = jdbcTemplate.queryForRowSet("SELECT genre_id, name FROM GENRES " +
                "WHERE genre_id=?", genreId);
        if (sqlQuery.next()) {
            Genre genre = Genre.builder()
                    .id(sqlQuery.getInt("genre_id"))
                    .name(sqlQuery.getString("name"))
                    .build();
            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());
            return Optional.of(genre);
        } else {
            throw new NotFoundException("Жанр с таким id не найден.");
        }
    }

    @Override
    public void addGenresForCurrentFilm(Film film) {
        if (Objects.isNull(film) || Objects.isNull(film.getGenres())) {
            return;
        }

        String sqlQuery = "INSERT INTO FILM_GENRE(film_id, genre_id) VALUES (?, ?)";
        List<Object[]> batchArgs = film.getGenres().stream()
                .map(g -> new Object[]{film.getId(), g.getId()})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sqlQuery, batchArgs);
    }

    public void addGenreNameToFilm(Film film) {
        if (Objects.isNull(film) || Objects.isNull(film.getGenres())) {
            return;
        }
        film.getGenres().forEach(g -> getGenreForId(g.getId()).map(Genre::getName).ifPresent(g::setName));
    }

    @Override
    public void updateGenresForCurrentFilm(Film film) {
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        addGenresForCurrentFilm(film);
    }

    @Override
    public Collection<Genre> getGenreForCurrentFilm(int filmId) {
        Collection<Genre> genreSet = new LinkedHashSet<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT id, film_id, genre_id FROM FILM_GENRE " +
                "ORDER BY FILM_GENRE.genre_id ASC");
        while (genreRows.next()) {
            if (genreRows.getLong("film_id") == filmId) {
                getGenreForId(genreRows.getInt("genre_id")).ifPresent(genreSet::add);
            }
        }
        return genreSet;
    }
}
