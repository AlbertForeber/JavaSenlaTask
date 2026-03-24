package com.senla.app.service.order;

import com.senla.app.exceptions.WrongId;
import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.status.BookStatus;
import com.senla.app.repository.OrderManagerRepository;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест сервиса заказов")
public class OrderServiceTest {

    @Mock
    private OrderManagerRepository orderManagerRepository;

    @Mock
    private StorageRepository storageRepository;

    @Mock
    private RequestManagerRepository requestManagerRepository;

    @Mock
    private UnitOfWork unitOfWork;

    private OrderService service;

    @BeforeEach
    public void init() {
        service = new OrderService(orderManagerRepository,
                storageRepository,
                requestManagerRepository,
                unitOfWork);
    }

    @Test
    @Tag("unit")
    @Tag("fast")
    @DisplayName("createOrder должен выбросить исключение, если переданы несуществующие книги")
    public void createOrderShouldThrowExceptionIfUnknownBookIds() {
        when(storageRepository.getBook(eq(2), anyBoolean())).thenReturn(null);

        Throwable throwable = assertThrows(WrongId.class,
                () -> service.createOrder(1, List.of(1), ""));
        assertEquals("указанных книг не существует", throwable.getMessage());

        verifyNoMoreInteractions(storageRepository);
        verifyNoInteractions(orderManagerRepository);
    }

    @Test
    @Tag("unit")
    @Tag("fast")
    @DisplayName("createOrder должен вернуть созданный заказ, если переданы существующие книги")
    public void createOrderShouldReturnOrderIfValidBookIds() {
        Book book = new Book(
                1,
                "",
                "",
                new GregorianCalendar(1, Calendar.JANUARY, 1),
                new GregorianCalendar(2, Calendar.JANUARY, 1),
                0,
                BookStatus.FREE,
                null
        );

        when(storageRepository.getBook(eq(1), anyBoolean())).thenReturn(book);

    }
}
