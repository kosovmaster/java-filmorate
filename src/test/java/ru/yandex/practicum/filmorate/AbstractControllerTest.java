package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class AbstractControllerTest {
    @Autowired
    protected FilmService filmService;

    @Autowired
    protected UserService userServiceImpl;
}
