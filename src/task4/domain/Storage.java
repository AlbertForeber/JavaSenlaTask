package task4.domain;

import task4.data.dto.Book;

public interface Storage {
    void addBook(Book book);
    Book getBook(String bookName);
    boolean removeBook(String bookName);
}
