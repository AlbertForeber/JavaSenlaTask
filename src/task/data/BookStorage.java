package task.data;


import task.data.comparators.book.*;
import task.data.dto.Book;
import task.data.dto.sortby.BookSortBy;
import task.data.dto.status.BookStatus;
import task.domain.Storage;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class BookStorage implements Storage {
    private final HashMap<String, Book> storage = new HashMap<>();

    public BookStorage() {
        addBook(new Book("I_Book1", "Desc1", 2025, 1, 1, BookStatus.FREE));
        addBook(new Book("G_Book2", "Desc2", 2025, 2, 2, BookStatus.FREE));
        addBook(new Book("F_Book3", "Desc3", 2025, 3, 3, BookStatus.FREE));
        addBook(new Book("H_Book4", "Desc4", 2025, 4, 4, BookStatus.FREE));
        addBook(new Book("C_Book5", "Desc5", 2025, 5, 5, BookStatus.FREE));
        addBook(new Book("D_Book6", "Desc6", 2025, 6, 6, BookStatus.RESERVED));
        addBook(new Book("E_Book7", "Desc7", 2025, 7, 7, BookStatus.RESERVED));
        addBook(new Book("B_Book8", "Desc8", 2025, 8, 8, BookStatus.RESERVED));
        addBook(new Book("A_Book9", "Desc9", 2025, 9, 9, BookStatus.FREE));
        addBook(new Book("J_Book10", "Desc10", 2025, 10, 10, BookStatus.RESERVED));
    }

    @Override
    public void addBook(Book book) {
        storage.put(book.getTitle(), book);
    }

    @Override
    public Book getBook(String bookName) {
        if (storage.containsKey(bookName)) {
            return storage.get(bookName);
        }
        return null;
    }

    @Override
    public boolean removeBook(String bookName) {
        return storage.remove(bookName) != null;
    }

    @Override
    public Book[] getSortedBooks(BookSortBy sortBy) {
        Comparator<Book> comparator = switch (sortBy) {
            case TITLE -> new BookTitleComparator();
            case PRICE -> new BookPriceComparator();
            case AVAILABILITY -> new BookAvailabilityComparator();
            case ADMISSION_DATE -> new BookAdmiDateComparator();
            case PUBLICATION_DATE -> new BookPublDateComparator();
            case DATE_PRICE -> new BookAdmiDatePriceComparator();
            case NO_SORT -> null;
        };

        Book[] arr = storage.values().toArray(new Book[0]);

        if (comparator != null) Arrays.sort(arr, comparator);
        return arr;
    }
}
