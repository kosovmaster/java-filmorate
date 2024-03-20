package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Qualifier
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Mpa> getAll() {
        Collection<Mpa> mpaList = new ArrayList<>();
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT MPA_ID, name FROM MPA");
        while (mpaRows.next()) {
            Mpa mpa = Mpa.builder()
                    .id(mpaRows.getInt("mpa_id"))
                    .name(mpaRows.getString("name"))
                    .build();
            mpaList.add(mpa);
        }
        return mpaList;
    }

    @Override
    public Optional<Mpa> getMpaForId(int mpaId) {
        String sql = "SELECT * FROM MPA where MPA_ID = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapRowToMpa, mpaId));
        } catch (RuntimeException e) {
            throw new NotFoundException("Рейтинг не найден");
        }
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    @Override
    public void addMpaToFilm(Film film) {
        getAll().forEach(mpa -> {
            if (Objects.equals(film.getMpa().getId(), mpa.getId())) {
                film.setMpa(mpa);
            }
        });
    }
}
