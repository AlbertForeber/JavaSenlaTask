package task.repository;

import task.model.entity.Book;
import task.model.entity.sortby.BookSortBy;

import java.util.List;

public interface StorageRepository {
    void addBook(Book book) throws IllegalArgumentException;
    Book getBook(int bookId);
    List<Book> getSortedBooks(BookSortBy sortBy);
    boolean removeBook(int bookId);
}
