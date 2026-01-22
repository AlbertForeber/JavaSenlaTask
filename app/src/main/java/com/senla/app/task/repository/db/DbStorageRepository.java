package com.senla.app.task.repository.db;

import com.senla.annotation.InjectTo;
import com.senla.app.task.db.dao.implementations.BookDao;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.sortby.BookSortBy;
import com.senla.app.task.model.entity.sortby.RequestSortBy;
import com.senla.app.task.repository.StorageRepository;
import com.senla.app.task.model.dto.BookDto;

import java.util.List;

public class DbStorageRepository implements StorageRepository {

    @InjectTo
    private BookDao bookDao;

    @Override
    public void addBook(BookDto bookDto) throws IllegalArgumentException {
        try {
            bookDao.save(bookDto);
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public void updateBook(BookDto bookDto) {
        try {
            bookDao.update(bookDto);
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public Book getBook(int bookId) {
        try {
            BookDto bookDto = bookDao.findById(bookId);

            if (bookDto == null) return null;
            return bookDto.toBusinessObject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public List<Book> getSortedBooks(BookSortBy sortBy) {
        StringBuilder additionSortQuery = new StringBuilder("ORDER BY ");

        switch (sortBy) {
            case TITLE -> additionSortQuery.append("title");
            case PUBLICATION_DATE -> additionSortQuery.append("publication_date");
            case ADMISSION_DATE -> additionSortQuery.append("admission_date");
            case PRICE -> additionSortQuery.append("price");
            case AVAILABILITY -> additionSortQuery.append("books.status");
            case DATE_PRICE -> additionSortQuery.append("admission_date, price");
        }

        try {
            return bookDao.findAll(
                    sortBy != BookSortBy.NO_SORT ? additionSortQuery.toString() : ""
            ).stream().map(BookDto::toBusinessObject).toList();
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
