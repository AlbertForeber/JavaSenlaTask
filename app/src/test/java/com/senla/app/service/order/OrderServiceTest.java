package com.senla.app.service.order;

import com.senla.app.exceptions.UnavailableAction;
import com.senla.app.exceptions.WrongId;
import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.Order;
import com.senla.app.model.entity.status.BookStatus;
import com.senla.app.model.entity.status.OrderStatus;
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

import java.util.ArrayList;
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

    private Book book;
    private Order order;

    @BeforeEach
    public void init() {
        service = new OrderService(orderManagerRepository,
                storageRepository,
                requestManagerRepository,
                unitOfWork);

        book = new Book(
                1,
                "",
                "",
                new GregorianCalendar(1, Calendar.JANUARY, 1),
                new GregorianCalendar(2, Calendar.JANUARY, 1),
                0,
                BookStatus.FREE,
                null
        );

        order = new Order(
                1,
                List.of(book),
                "",
                100,
                new GregorianCalendar(1, Calendar.JANUARY, 1),
                OrderStatus.NEW
        );
    }

    @Test
    @Tag("unit")
    @Tag("fast")
    @DisplayName("createOrder должен выбросить исключение, если переданы несуществующие книги")
    public void createOrderShouldThrowExceptionIfUnknownBookIds() {
        when(storageRepository.getBook(eq(2), anyBoolean())).thenReturn(null);

        Throwable throwable = assertThrows(WrongId.class,
                () -> service.createOrder(1, List.of(2), ""));
        assertEquals("указанных книг не существует", throwable.getMessage());

        verifyNoMoreInteractions(storageRepository);
        verifyNoInteractions(orderManagerRepository);
    }

    @Test
    @Tag("unit")
    @Tag("fast")
    @DisplayName("createOrder должен вернуть созданный заказ, если переданы существующие книги")
    public void createOrderShouldReturnOrderIfValidBookIds() {
        when(storageRepository.getBook(eq(1), anyBoolean())).thenReturn(book);
        Order result = service.createOrder(1, List.of(1), "");

        assertEquals(BookStatus.RESERVED, book.getStatus(), "Статус книги должен быть изменен на RESERVED");

        assertAll("свойства возвращенного Order",
                () -> assertEquals(1, result.getId(),
                        "ID заказа должен совпадать с переданным"),
                () -> assertEquals(1, result.getOrderedBooks().size(),
                        "В заказе должна быть одна книга"),
                () -> assertSame(book, result.getOrderedBooks().getFirst(),
                        "В заказе должна быть книга из репозитория")

        );
        verifyNoInteractions(requestManagerRepository); // Запрос не должен добавляться, т.к. книга доступна
    }

    @Test
    @Tag("unit")
    @Tag("fast")
    @DisplayName("createOrder должен выбросить исключение, если заказа с таким ID не существует")
    public void cancelOrderShouldThrowExceptionIfUnknownOrderId() {
        when(orderManagerRepository.getOrder(eq(2), anyBoolean())).thenReturn(null);

        Throwable throwable = assertThrows(WrongId.class,
                () -> service.cancelOrder(2));

        assertTrue(throwable.getMessage().contains("2"), "Сообщение об ошибке должно содержать ID заказа");
        verifyNoInteractions(storageRepository);
    }

    @Test
    @Tag("unit")
    @Tag("fast")
    @DisplayName("cancelOrder должен вернуть отмененный заказ, если заказ с таким ID существует")
    public void cancelOrderShouldReturnOrderIfValidOrderId() {
        book.setStatus(BookStatus.RESERVED, order);

        when(orderManagerRepository.getOrder(eq(1), anyBoolean())).thenReturn(order);
        Order result = service.cancelOrder(1);

        assertEquals(BookStatus.FREE, book.getStatus(), "Зарезервированные книги должны освободиться");
        assertAll("свойства возвращенного Order",
                () -> assertEquals(1, result.getId(),
                        "ID отмененного заказа должен совпадать с переданным"),
                () -> assertEquals(OrderStatus.CANCELED, result.getStatus(), "Заказ должен быть отменен")
        );

        verify(storageRepository, times(1)).updateBook(any(Book.class));
    }

    @Test
    @Tag("unit")
    @Tag("fast")
    @DisplayName("changeOrderStatus должен выбросить исключение, если заказ с таким ID не существует")
    public void changeStatusShouldThrowExceptionWhenWrongOrderId() {
        when(orderManagerRepository.getOrder(eq(2), anyBoolean())).thenReturn(null);

        Throwable throwable = assertThrows(WrongId.class,
                () -> service.changeOrderStatus(2, OrderStatus.COMPLETED));

        assertTrue(throwable.getMessage().contains("2"), "Сообщение об ошибке должно содержать ID заказа");
        verifyNoMoreInteractions(orderManagerRepository);
        verifyNoInteractions(storageRepository);
    }

    @Test
    @Tag("unit")
    @Tag("fast")
    @DisplayName("changeOrderStatus должен выбросить исключение, если некоторые книги недоступны")
    public void changeStatusShouldThrowExceptionWhenBooksUnavailable() {
        Order order2 = new Order(
                2,
                List.of(book),
                "",
                100,
                new GregorianCalendar(1, Calendar.JANUARY, 1),
                OrderStatus.NEW
        );
        book.setStatus(BookStatus.RESERVED, order);
        when(orderManagerRepository.getOrder(eq(2), anyBoolean())).thenReturn(order2);

        UnavailableAction exception = assertThrows(UnavailableAction.class,
                () -> service.changeOrderStatus(2, OrderStatus.COMPLETED));

        assertTrue(exception.getReason().contains("1"),
                "Сообщение об ошибке должно содержать ID недоступной книги");
        verifyNoMoreInteractions(orderManagerRepository);
        verifyNoInteractions(storageRepository);
    }

    @Test
    @Tag("unit")
    @Tag("fast")
    @DisplayName("changeOrderStatus должен вернуть измененный заказ, если все книги доступны")
    public void changeStatusShouldReturnModifiedOrderWhenBooksAvailable() {
        when(orderManagerRepository.getOrder(eq(1), anyBoolean())).thenReturn(order);

        Order result = service.changeOrderStatus(1, OrderStatus.COMPLETED);
        assertEquals(BookStatus.SOLD_OUT, book.getStatus(), "Статус книги должен измениться на SOLD OUT");
        assertAll("свойства возвращенного Order",
                () -> assertEquals(1, result.getId(),
                        "ID измененного заказа должен совпадать с переданным"),
                () -> assertEquals(OrderStatus.COMPLETED, result.getStatus(),
                        "Статус заказа должен измениться на COMPLETED"),
                () -> assertTrue(Calendar.getInstance().getTimeInMillis() - result.getCompletionDate().getTimeInMillis() < 86400000,
                        "Дата создания должна быть текущей датой")
        );
        verify(storageRepository, times(1)).updateBook(any(Book.class));
    }
}
