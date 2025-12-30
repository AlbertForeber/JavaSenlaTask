package com.senla.app.task.repository.db;


import com.senla.annotation.InjectTo;
import com.senla.app.task.db.dao.implementations.BookDao;
import com.senla.app.task.model.comparators.book.*;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.sortby.BookSortBy;
import com.senla.app.task.repository.StorageRepository;
import com.senla.app.task.repository.dto.BookDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DbStorageRepository implements StorageRepository {

    @InjectTo
    private BookDao bookDao;

    @Override
    public void addBook(BookDto bookDto) throws IllegalArgumentException {
        try {
            bookDao.save(bookDto);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public void updateBook(BookDto bookDto) {
        try {
            bookDao.update(bookDto);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public Book getBook(int bookId) {
        try {
            return bookDao.findById(bookId).toBusinessObject();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public List<Book> getSortedBooks(BookSortBy sortBy) {
        Comparator<Book> comparator = switch (sortBy) {
            case TITLE -> new BookTitleComparator();
            case PRICE -> new BookPriceComparator();
            case AVAILABILITY -> new BookAvailabilityComparator();
            case ADMISSION_DATE -> new BookAdmiDateComparator();
            case PUBLICATION_DATE -> new BookPublDateComparator();
            case DATE_PRICE -> new BookAdmiDatePriceComparator();
            case NO_SORT -> null;
        };

        try {
            List<Book> arr = new ArrayList<>(bookDao.findAll().stream().map(BookDto::toBusinessObject).toList());

            if (comparator != null) arr.sort(comparator);
            return arr;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public boolean removeBook(int bookId) {
        try {
            bookDao.delete(bookId);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }

        return true;
    }
}
