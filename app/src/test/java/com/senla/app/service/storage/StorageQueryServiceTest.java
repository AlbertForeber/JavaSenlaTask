package com.senla.app.service.storage;

import com.senla.app.exceptions.ResourceNotFound;
import com.senla.app.exceptions.WrongId;
import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.sortby.BookSortBy;
import com.senla.app.model.entity.status.BookStatus;
import com.senla.app.repository.StorageRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест сервиса получения книг")
public class StorageQueryServiceTest {

    @Mock
    private StorageRepository repository;

    @Mock
    private UnitOfWork unitOfWork;

    private StorageQueryService service;

    @BeforeEach
    public void init() {
        service = new StorageQueryService(repository, 12, unitOfWork);
    }

    @Nested
    @DisplayName("Когда книг нет")
    class WhenEmpty {

        private List<Book> repoReturn;

        @BeforeEach
        public void init() {
            repoReturn = new ArrayList<>();
        }

        @ParameterizedTest
        @DisplayName("getSorted должен выбросить исключение")
        @Tag("unit")
        @EnumSource(BookSortBy.class)
        public void getSortedShouldThrowExceptionWhenEmpty(BookSortBy sortBy) {
            when(repository.getSortedBooks(any(BookSortBy.class), anyBoolean())).thenReturn(repoReturn);

            Throwable throwable = assertThrows(ResourceNotFound.class, () -> service.getSorted(sortBy));
            assertEquals("книг нет", throwable.getMessage(),
                    "Исключение должно содержать сообщение 'книг нет");
        }

        @Test
        @DisplayName("getHardToSell должен выбросить исключение")
        @Tag("unit")
        public void getHardToSellShouldThrowExceptionWhenEmpty() {
            when(repository.getSortedBooks(any(BookSortBy.class), anyBoolean())).thenReturn(repoReturn);

            Throwable throwable = assertThrows(ResourceNotFound.class,
                    () -> service.getHardToSell(1, 1, 1));
            assertEquals("книг нет", throwable.getMessage(),
                    "Исключение должно содержать сообщение 'книг нет");
        }

        @Nested
        @DisplayName("Когда есть одна книга")
        class AfterAddingOne {

            @BeforeEach
            public void init() {
                repoReturn.add(new Book(
                        1,
                        "",
                        "",
                        new GregorianCalendar(1, Calendar.JANUARY, 1),
                        new GregorianCalendar(2, Calendar.JANUARY, 1),
                        0,
                        BookStatus.FREE,
                        null
                ));
            }

            @ParameterizedTest
            @DisplayName("getSorted должно вернуть список")
            @Tag("unit")
            @EnumSource(BookSortBy.class)
            public void getSortedShouldReturnListWhenNotEmpty(BookSortBy sortBy) {
                when(repository.getSortedBooks(any(BookSortBy.class), anyBoolean())).thenReturn(repoReturn);
                List<Book> result = service.getSorted(sortBy);

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
            @DisplayName("getHardToSell должен вернуть пустой список, если время ликвидности не истекло")
            @Tag("unit")
            public void getHardToSellShouldReturnEmptyListWhenLiquidMonthsHaveNotPassed() {
                when(repository.getSortedBooks(any(BookSortBy.class), anyBoolean())).thenReturn(repoReturn);

                assertEquals(0, service.getHardToSell(2, 2, 1).size(),
                        "Список должен быть пуст");
            }

            @Test
            @DisplayName("getHardToSell должен вернуть список с книгой, если время ликвидности истекло")
            @Tag("unit")
            public void getHardToSellShouldReturnListWhenLiquidMonthsHavePassed() {
                when(repository.getSortedBooks(any(BookSortBy.class), anyBoolean())).thenReturn(repoReturn);
                List<Book> result = service.getHardToSell(3, 1, 1);

                assertAll("свойства списка неликвидных книг",
                        () -> assertEquals(1, result.size(),
                                "Возвращаемый список должен содержать один элемент"),
                        () -> assertEquals(1, result.getFirst().getId(),
                                "Единственный элемент в списке должен иметь идентификатор 1")
                );
            }

            @Test
            @DisplayName("getBookDescription должен выбросить исключение, если книга с таким ID не найдена")
            @Tag("unit")
            public void getBookDescriptionShouldThrowExceptionWhenWrongId() {
                when(repository.getBook(eq(2), anyBoolean())).thenReturn(null);

                Throwable throwable = assertThrows(WrongId.class, () -> service.getBookDescription(2));
                assertTrue(throwable.getMessage().contains("2"),
                        "Сообщение об ошибке должно содержать ID книги");
            }

            @Test
            @DisplayName("getBookDescription должен вернуть описание, если книга с таким ID найдена")
            @Tag("unit")
            public void getBookDescriptionShouldReturnDescriptionWhenValidId() {
                when(repository.getBook(eq(1), anyBoolean())).thenReturn(repoReturn.getFirst());

                assertEquals(repoReturn.getFirst().getDescription(), service.getBookDescription(1),
                        "Возвращенное описание должно совпадать с найденной книгой");
            }
        }
    }
}
