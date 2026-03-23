package com.senla.app.service.request;

import com.senla.app.exceptions.ResourceNotFound;
import com.senla.app.exceptions.WrongId;
import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.Request;
import com.senla.app.model.entity.sortby.RequestSortBy;
import com.senla.app.model.entity.status.BookStatus;
import com.senla.app.repository.RequestManagerRepository;
import com.senla.app.repository.StorageRepository;
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
@DisplayName("Тест создания запросов")
public class RequestServiceTest {

    @Mock
    private StorageRepository storageRepository;

    @Mock
    private RequestManagerRepository requestManagerRepository;

    @Mock
    private UnitOfWork unitOfWork;

    private RequestService service;

    @BeforeEach
    public void init() {
        service = new RequestService(requestManagerRepository, storageRepository, unitOfWork);
    }

    @Test
    @DisplayName("должен выбросить исключение, если книги нет")
    @Tag("unit")
    @Tag("service")
    public void shouldThrowExceptionWhenNoBook() {
        when(storageRepository.getBook(1, anyBoolean())).thenReturn(null);

        Throwable throwable = assertThrows(WrongId.class, () -> service.createRequest(1));
        assertTrue(throwable.getMessage().contains("1")); // В сообщении об ошибке должен быть id книги

        // Не должно быть взаимодействий ввиду исключения
        verifyNoInteractions(requestManagerRepository);
    }

    @Test
    @DisplayName("должен выбросить исключение, если книги нет")
    @Tag("unit")
    @Tag("service")
    public void shouldReturnIfThereBook() {
        Book book = new Book(1, "", "", 1, 1, 1, BookStatus.FREE);

        when(storageRepository.getBook(1, anyBoolean())).thenReturn(book);
        when(requestManagerRepository.addRequest(book)).thenReturn(new Request(1, book, 1));

        Request result = service.createRequest(1);
        assertEquals(1, result.getId());
        assertEquals(book.getId(), result.getBook().getId());
    }
}