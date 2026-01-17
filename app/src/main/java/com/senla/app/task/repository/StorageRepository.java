package com.senla.app.task.repository;

import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.sortby.BookSortBy;
import com.senla.app.task.model.dto.BookDto;

import java.util.List;

public interface StorageRepository {
    void addBook(BookDto bookDto) throws IllegalArgumentException;
    void updateBook(BookDto bookDto);
    Book getBook(int bookId);
    List<Book> getSortedBooks(BookSortBy sortBy);
    boolean removeBook(int bookId);
}
