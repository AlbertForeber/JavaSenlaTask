package task4.data;


import task4.data.dto.Book;
import task4.data.dto.BookStatus;
import task4.domain.Storage;

import java.util.HashMap;

public class BookStorage implements Storage {
    private final HashMap<String, Book> storage = new HashMap<>();

    public BookStorage() {
        addBook(new Book("Book1", "Desc1", 2001, 1, 1, BookStatus.FREE));
        addBook(new Book("Book2", "Desc2", 2002, 2, 2, BookStatus.FREE));
        addBook(new Book("Book3", "Desc3", 2003, 3, 3, BookStatus.FREE));
        addBook(new Book("Book4", "Desc4", 2004, 4, 4, BookStatus.FREE));
        addBook(new Book("Book5", "Desc5", 2005, 5, 5, BookStatus.FREE));
        addBook(new Book("Book6", "Desc6", 2006, 6, 6, BookStatus.RESERVED));
        addBook(new Book("Book7", "Desc7", 2007, 7, 7, BookStatus.RESERVED));
        addBook(new Book("Book8", "Desc8", 2008, 8, 8, BookStatus.RESERVED));
        addBook(new Book("Book9", "Desc9", 2009, 9, 9, BookStatus.FREE));
        addBook(new Book("Book10", "Desc10", 2010, 10, 10, BookStatus.RESERVED));
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
}
