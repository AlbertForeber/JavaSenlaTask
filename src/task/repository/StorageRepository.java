package task.repository;

import task.model.entity.Book;
import task.model.entity.sortby.BookSortBy;

import java.util.List;

public interface StorageRepository {
    void addBook(Book book);
    Book getBook(String bookName);
    List<Book> getSortedBooks(BookSortBy sortBy);
    boolean removeBook(String bookName);
}
