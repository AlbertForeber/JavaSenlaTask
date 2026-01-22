package com.senla.app.task.repository.db;

import com.senla.annotation.InjectTo;
import com.senla.app.task.db.dao.GenericDao;
import com.senla.app.task.db.dao.hibernate_implementations.BookDao;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.sortby.BookSortBy;
import com.senla.app.task.repository.StorageRepository;

import java.util.List;

public class DbStorageRepository implements StorageRepository {

    @InjectTo(useImplementation = BookDao.class)
    GenericDao<Book, Integer, BookSortBy> bookDao;

    @Override
    public void addBook(Book book) throws IllegalArgumentException {
        try {
            bookDao.save(book);
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public void updateBook(Book book) {
        try {
            bookDao.update(book);
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public Book getBook(int bookId, boolean getLinkedObjects) {
        try {
            return bookDao.findById(bookId, getLinkedObjects);
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public List<Book> getSortedBooks(BookSortBy sortBy, boolean getLinkedObjects) {
        try {
            return bookDao.findAll(sortBy != BookSortBy.NO_SORT ? sortBy : null, getLinkedObjects);
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public boolean removeBook(int bookId) {
        try {
            bookDao.delete(bookId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }

        return true;
    }
}
