package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;
import ru.yandex.practicum.filmorate.service.UserServiceImpl;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class AbstractControllerTest {
    @Autowired
    protected FilmServiceImpl filmService;

    @Autowired
    protected UserServiceImpl userServiceImpl;
}
