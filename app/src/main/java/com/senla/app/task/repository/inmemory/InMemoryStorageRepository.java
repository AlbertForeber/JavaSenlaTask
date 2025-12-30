package com.senla.app.task.repository.inmemory;


import com.senla.app.task.repository.StorageRepository;
import com.senla.app.task.model.comparators.book.*;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.sortby.BookSortBy;
import com.senla.app.task.model.entity.status.BookStatus;
import com.senla.app.task.repository.dto.BookDto;

import java.util.*;

public class InMemoryStorageRepository implements StorageRepository {
    private final HashMap<Integer, Book> storage = new HashMap<>();

    public InMemoryStorageRepository() {
        storage.put(1, new Book(1, "I_Book1", "Desc1", 2025, 1, 1, BookStatus.FREE));
        storage.put(2, new Book(2, "G_Book2", "Desc2", 2025, 2, 2, BookStatus.FREE));
        storage.put(3, new Book(3, "F_Book3", "Desc3", 2025, 3, 3, BookStatus.FREE));
        storage.put(4, new Book(4, "H_Book4", "Desc4", 2025, 4, 4, BookStatus.FREE));
        storage.put(5, new Book(5, "C_Book5", "Desc5", 2025, 5, 5, BookStatus.FREE));

        storage.put(6, new Book(6, "D_Book6", "Desc6", 2025, 6, 6, BookStatus.FREE));
        storage.get(6).setStatus(BookStatus.RESERVED, "reservist6");

        storage.put(7, new Book(7, "E_Book7", "Desc7", 2025, 7, 7, BookStatus.FREE));
        storage.get(7).setStatus(BookStatus.RESERVED, "reservist7");

        storage.put(8, new Book(8, "B_Book8", "Desc8", 2025, 8, 8, BookStatus.FREE));
        storage.get(8).setStatus(BookStatus.RESERVED, "reservist8");

        storage.put(9, new Book(9, "A_Book9", "Desc9", 2025, 9, 9, BookStatus.FREE));

        storage.put(10, new Book(10, "J_Book10", "Desc10", 2025, 10, 10, BookStatus.RESERVED));
        storage.get(10).setStatus(BookStatus.RESERVED, "reservist10");
    }

    @Override
    public void addBook(BookDto bookDto) throws IllegalArgumentException {
        storage.put(bookDto.getId(), bookDto.toBusinessObject());
    }

    @Override
    public void updateBook(BookDto bookDto) {
        addBook(bookDto);
    }

    @Override
    public Book getBook(int bookId) {
        if (storage.containsKey(bookId)) {
            return storage.get(bookId);
        }
        return null;
    }

    @Override
    public boolean removeBook(int bookId) {
        return storage.remove(bookId) != null;
    }

    @Override
    public List<Book> getSortedBooks(BookSortBy sortBy) {
        Comparator<Book> comparator = switch (sortBy) {
            case TITLE -> new BookTitleComparator();
            case PRICE -> new BookPriceComparator();
            case AVAILABILITY -> new BookAvailabilityComparator();
            case ADMISSION_DATE -> new BookAdmiDateComparator();
            case PUBLICATION_DATE -> new BookPublDateComparator();
            case DATE_PRICE -> new BookAdmiDatePriceComparator();
            case NO_SORT -> null;
        };

        List<Book> arr = new ArrayList<>(storage.values().stream().toList());

        if (comparator != null) arr.sort(comparator);
        return arr;
    }
}
