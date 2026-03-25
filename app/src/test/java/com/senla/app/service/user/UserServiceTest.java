package com.senla.app.service.user;

import com.senla.app.model.entity.auth.User;
import com.senla.app.repository.UserRepository;
import com.senla.app.service.auth.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест сервиса пользователей")
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    private UserService service;

    @BeforeEach
    public void init() {
        service = new UserService(repository);
    }

    @Test
    @Tag("unit")
    @Tag("test")
    @DisplayName("должен вернуть сохраненного пользователя")
    public void shouldReturnSavedUser() {
        User user = new User(1, "username", "pass", Collections.emptyList());
        when(repository.saveUser(any(User.class))).thenReturn(user);

        assertEquals(user.getUsername(), service.createUser(user).getUsername(),
                "Имя сохраненного и переданного пользователя должны совпадать");
    }
}