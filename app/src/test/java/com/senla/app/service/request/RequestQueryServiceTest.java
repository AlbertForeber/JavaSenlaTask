package com.senla.app.service.request;

import com.senla.app.exceptions.ResourceNotFound;
import com.senla.app.model.entity.Request;
import com.senla.app.model.entity.sortby.RequestSortBy;
import com.senla.app.repository.RequestManagerRepository;
import com.senla.app.service.unit_of_work.UnitOfWork;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест сервиса получения запросов")
public class RequestQueryServiceTest {

    @Mock
    private UnitOfWork unitOfWork;

    @Mock
    private RequestManagerRepository repository;

    private RequestQueryService service;

    @BeforeEach
    public void init() {
        // Нельзя использовать @InjectMocks
        // Ввиду наличия своих квалификаторов (@Db, @Hibernate)
        service = new RequestQueryService(repository, unitOfWork);
    }

    @Nested
    @DisplayName("Когда запросов нет")
    class WhenEmpty {

        private List<Request> repoReturn;

        @BeforeEach
        public void init() {
            repoReturn = new ArrayList<>();
        }

        @Test
        @DisplayName("должен выбросить исключение")
        @Tag("unit")
        @Tag("service")
        public void shouldThrowExceptionWhenEmpty() {
            when(repository.getSortedRequests(any(RequestSortBy.class))).thenReturn(repoReturn);

            Throwable throwable = assertThrows(ResourceNotFound.class, () -> service.getSorted(RequestSortBy.NO_SORT));
            assertEquals("запросов нет", throwable.getMessage(),
                    "Исключение должно содержать сообщение 'запросов нет'");
        }

        @Nested
        @DisplayName("Когда есть запрос")
        class AfterAddingOne {

            @BeforeEach
            public void init() {
                repoReturn.add(new Request(1, null, 1));
            }

            @Test
            @DisplayName("возврат непустого списка запросов")
            @Tag("unit")
            @Tag("service")
            public void shouldReturnRequestListWhenNotEmpty() {
                when(repository.getSortedRequests(any(RequestSortBy.class))).thenReturn(repoReturn);
                List<Request> result = service.getSorted(RequestSortBy.NO_SORT);

                assertAll("свойства списка Request",
                        () -> assertEquals(1, result.size(),
                                "Возвращаемый список должен содержать один элемент"),
                        () -> assertEquals(1, result.getFirst().getId(),
                                "Единственный элемент в списке должен иметь идентификатор 1")
                );

                // Провяряем, что Deprecated функционал не используется
                verifyNoInteractions(unitOfWork);
            }
        }
    }
}