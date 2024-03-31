package ru.yandex.practicum.filmorate.storage.like;

import java.util.Collection;

public interface LikeStorage {
    Collection<Integer> getLikesForCurrentFilm(int id);
}
