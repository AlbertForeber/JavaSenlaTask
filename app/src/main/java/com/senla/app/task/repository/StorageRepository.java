package com.senla.app.task.repository;

import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.sortby.BookSortBy;

import java.util.List;

public interface StorageRepository {

    void addBook(Book book);
    void updateBook(Book book);
    Book getBook(int bookId, boolean getLinkedObjects);
    List<Book> getSortedBooks(BookSortBy sortBy, boolean getLinkedObjects);
    boolean removeBook(int bookId);
}
