package task.domain;

import task.data.dto.Book;
import task.data.dto.sortby.BookSortBy;

public interface Storage {
    void addBook(Book book);
    Book getBook(String bookName);
    Book[] getSortedBooks(BookSortBy sortBy);
    boolean removeBook(String bookName);
}
