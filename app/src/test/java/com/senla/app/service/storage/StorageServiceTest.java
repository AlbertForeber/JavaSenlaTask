package com.senla.app.service.storage;

import com.senla.app.exceptions.WrongId;
import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.status.BookStatus;
import com.senla.app.repository.RequestManagerRepository;
import com.senla.app.repository.StorageRepository;
import com.senla.app.service.unit_of_work.UnitOfWork;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест сервиса книг")
public class StorageServiceTest {

    @Mock
    private StorageRepository storageRepository;

    @Mock
    private RequestManagerRepository requestManagerRepository;

    @Mock
    private UnitOfWork unitOfWork;

    private StorageService service;

    private Book repoReturn;

    @BeforeEach
    public void init() {
        service = new StorageService(
                storageRepository,
                requestManagerRepository,
                unitOfWork,
                true);

        repoReturn = new Book(
                1,
                "",
                "",
                new GregorianCalendar(1, Calendar.JANUARY, 1),
                new GregorianCalendar(2, Calendar.JANUARY, 1),
                0,
                BookStatus.FREE,
                null
        );
    }

    @Test
    @DisplayName("writeOff должен выбросить исключение, если книга с таким ID не найдена")
    @Tag("unit")
    @Tag("fast")
    public void writeOffShouldThrowExceptionWhenWrongId() {
        when(storageRepository.getBook(eq(2), anyBoolean())).thenReturn(null);

        Throwable throwable = assertThrows(WrongId.class, () -> service.writeOffBook(2));
        assertTrue(throwable.getMessage().contains("2"),
                "Сообщение об ошибке должно содержать ID книги");

        verifyNoMoreInteractions(storageRepository);
    }

    @Test
    @DisplayName("writeOff должен вернуть книгу со статусом SOLD_OUT, если книга найдена")
    @Tag("unit")
    @Tag("fast")
    public void writeOffShouldReturnBookWithNewStatusWhenValidId() {
        when(storageRepository.getBook(eq(1), anyBoolean())).thenReturn(repoReturn);
        Book result = service.writeOffBook(1);

        verify(storageRepository, times(1)).updateBook(repoReturn);

        assertAll(
                () -> assertEquals(BookStatus.SOLD_OUT, result.getStatus(),
        "Статус должен измениться на SOLD_OUT")
        );
    }

    @Test
    @DisplayName("addBookToStorage должен выбросить исключение, если книга с таким ID не найдена")
    @Tag("unit")
    @Tag("fast")
    public void addBookToStorageShouldThrowExceptionWhenWrongId() {
        when(storageRepository.getBook(eq(2), anyBoolean())).thenReturn(null);

        Throwable throwable = assertThrows(WrongId.class, () -> service.addBookToStorage(2));
        assertTrue(throwable.getMessage().contains("2"),
                "Сообщение об ошибке должно содержать ID книги");

        verifyNoMoreInteractions(storageRepository);
    }

    @Test
    @DisplayName("addBookToStorage должен вернуть книгу со статусом FREE, если книга найдена")
    @Tag("unit")
    @Tag("fast")
    public void addBookToStorageShouldReturnBookWithNewStatusWhenValidId() {
        when(storageRepository.getBook(eq(1), anyBoolean())).thenReturn(repoReturn);
        Book result = service.addBookToStorage(1);

        verify(storageRepository, times(1)).updateBook(repoReturn);
        verify(requestManagerRepository, times(1)).cancelRequests(repoReturn.getTitle());

        assertAll(
                () -> assertEquals(BookStatus.FREE, result.getStatus(),
                        "Статус должен измениться на FREE")
        );
    }

}
