package com.senla.app.service.user;

import com.senla.app.exceptions.ResourceNotFound;
import com.senla.app.model.entity.auth.User;
import com.senla.app.repository.UserRepository;
import com.senla.app.service.auth.user.UserQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест получения пользователей")
public class UserQueryServiceTest {

    @Mock
    private UserRepository repository;

    private UserQueryService service;

    @BeforeEach
    public void init() {
        service = new UserQueryService(repository);
    }

    @Test
    @Tag("unit")
    @Tag("fast")
    @DisplayName("должен выбросить ошибку, если пользователя с таким псевдонимом нет")
    public void shouldThrowExceptionWhenInvalidUsername() {
        when(repository.getUserByUsername(any(String.class))).thenReturn(null);

        Throwable throwable = assertThrows(ResourceNotFound.class, () -> service.getUserByUsername("username"));
        assertTrue(throwable.getMessage().contains("username"),
                "Сообщение об ошибке должно содержать псевдоним ненайденного пользователя");
    }

    @Test
    @Tag("unit")
    @Tag("fast")
    @DisplayName("должен вернуть сущность пользователя, если пользователь с таким псевдонимом есть")
    public void shouldReturnUserWhenValidUsername() {
        User user = new User(1, "username", "pass", Collections.emptyList());
        when(repository.getUserByUsername("username")).thenReturn(user);

        assertSame(user, service.getUserByUsername("username"),
                "Должен вернуть сущность пользователя, заданную изначально");
    }
}
