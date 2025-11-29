package com.senla.app.task.repository;

import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.sortby.BookSortBy;

import java.util.List;

public interface StorageRepository {
    void addBook(Book book) throws IllegalArgumentException;
    Book getBook(int bookId);
    List<Book> getSortedBooks(BookSortBy sortBy);
    boolean removeBook(int bookId);
}
