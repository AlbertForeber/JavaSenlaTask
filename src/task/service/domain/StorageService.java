package task.service.domain;

import task.model.entity.Book;
import task.model.entity.sortby.BookSortBy;

public interface StorageService {
    void addBook(Book book);
    Book getBook(String bookName);
    Book[] getSortedBooks(BookSortBy sortBy);
    boolean removeBook(String bookName);
}
