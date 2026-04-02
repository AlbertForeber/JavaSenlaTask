package com.senla.app.service.order;

import com.senla.app.exceptions.ResourceNotFound;
import com.senla.app.exceptions.WrongId;
import com.senla.app.model.entity.Order;
import com.senla.app.model.entity.sortby.OrderSortBy;
import com.senla.app.model.entity.status.OrderStatus;
import com.senla.app.repository.OrderManagerRepository;
import com.senla.app.service.unit_of_work.UnitOfWork;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест сервиса получения заказа")
public class OrderQueryServiceTest {

    @Mock
    private OrderManagerRepository repository;

    @Mock
    private UnitOfWork unitOfWork;

    private OrderQueryService service;

    @BeforeEach
    public void init() {
        service = new OrderQueryService(repository, unitOfWork);
    }

    @Nested
    @DisplayName("Когда заказов нет")
    class WhenEmpty {

        private List<Order> repoReturn;

        @BeforeEach
        public void init() {
            repoReturn = new ArrayList<>();
        }

        @ParameterizedTest
        @DisplayName("getSorted должен выбросить исключение")
        @Tag("unit")
        @EnumSource(OrderSortBy.class)
        public void getSortedShouldThrowExceptionWhenEmpty(OrderSortBy sortBy) {
            when(repository.getSortedOrders(any(OrderSortBy.class), anyBoolean())).thenReturn(repoReturn);

            Throwable throwable = assertThrows(ResourceNotFound.class, () -> service.getSorted(sortBy));
            assertEquals("заказов нет", throwable.getMessage(),
                    "Исключение должно содержать сообщение 'заказов нет");
        }

        @ParameterizedTest
        @DisplayName("getCompletedOrdersInInterval должен выбросить исключение")
        @Tag("unit")
        @EnumSource(OrderSortBy.class)
        public void getCompletedShouldThrowExceptionWhenEmpty(OrderSortBy sortBy) {
            when(repository.getSortedOrders(any(OrderSortBy.class), anyBoolean())).thenReturn(repoReturn);

            Throwable throwable = assertThrows(ResourceNotFound.class, () -> service.getCompletedOrdersInInterval(
                    1, 1, 1, 1, 1, 1, false
            ));
            assertEquals("заказов нет", throwable.getMessage(),
                    "Исключение должно содержать сообщение 'заказов нет");
        }

        // Два метода связанные с интервалом пропущены, т.к. по сути просто вызывают метод выше
        // Т.е. успех их выполнения полностью зависит от getCompletedOrdersInInterval

        @Nested
        @DisplayName("Когда есть один заказ")
        class AfterAddingOne {

            @BeforeEach
            public void init() {
                repoReturn.add(new Order(
                        1,
                        null,
                        "",
                        100,
                        new GregorianCalendar(1, Calendar.JANUARY, 1),
                        OrderStatus.COMPLETED
                ));
            }

            @ParameterizedTest
            @DisplayName("getSorted должно вернуть список")
            @Tag("unit")
            @EnumSource(OrderSortBy.class)
            public void getSortedShouldReturnListWhenNotEmpty(OrderSortBy sortBy) {
                when(repository.getSortedOrders(any(OrderSortBy.class), anyBoolean())).thenReturn(repoReturn);
                List<Order> result = service.getSorted(sortBy);

                assertAll("свойства списка Book",
                        () -> assertEquals(1, result.size(),
                                "Возвращаемый список должен содержать один элемент"),
                        () -> assertEquals(1, result.getFirst().getId(),
                                "Единственный элемент в списке должен иметь идентификатор 1")
                );

                // Проверяем, что Deprecated функционал не используется
                verifyNoInteractions(unitOfWork);
            }

            @Test
            @DisplayName("getCompletedOrdersInInterval должен вернуть пустой список, если интервал не содержит заказов")
            @Tag("unit")
            public void getCompletedShouldReturnEmptyListWhenNotInInterval() {
                when(repository.getSortedOrders(any(OrderSortBy.class), anyBoolean())).thenReturn(repoReturn);

                assertEquals(0, service.getCompletedOrdersInInterval(
                        2, 2, 2, 2, 2, 2, false).size(),
                        "Список должен быть пуст");
            }

            @Test
            @DisplayName("getCompletedOrdersInInterval должен вернуть список с заказом, если интервал его включает")
            @Tag("unit")
            public void getCompletedShouldReturnListWhenInInterval() {
                when(repository.getSortedOrders(any(OrderSortBy.class), anyBoolean())).thenReturn(repoReturn);
                List<Order> result = service.getCompletedOrdersInInterval(
                        0, 0, 0, 2, 2, 2, false);

                assertAll("свойства списка неликвидных книг",
                        () -> assertEquals(1, result.size(),
                                "Возвращаемый список должен содержать один элемент"),
                        () -> assertEquals(1, result.getFirst().getId(),
                                "Единственный элемент в списке должен иметь идентификатор 1")
                );
            }

            @Test
            @DisplayName("getOrderDetails должен выбросить исключение, если заказ с таким ID не найден")
            @Tag("unit")
            public void getOrderDetailsShouldThrowExceptionWhenWrongId() {
                when(repository.getOrder(eq(2), anyBoolean())).thenReturn(null);

                Throwable throwable = assertThrows(WrongId.class, () -> service.getOrderDetails(2));
                assertTrue(throwable.getMessage().contains("2"),
                        "Сообщение об ошибке должно содержать ID заказа");
            }

            @Test
            @DisplayName("getOrderDetails должен вернуть заказ, если заказ с таким ID найден")
            @Tag("unit")
            public void getOrderDetailsShouldReturnDescriptionWhenValidId() {
                when(repository.getOrder(eq(1), anyBoolean())).thenReturn(repoReturn.getFirst());

                assertEquals(repoReturn.getFirst().getId(), service.getOrderDetails(1).getId(),
                        "Возвращенный заказ должен совпадать с найденным");
            }
        }
    }
}
