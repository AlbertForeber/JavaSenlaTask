package com.senla.app.task.repository.db;

import com.senla.annotation.InjectTo;
import com.senla.app.task.db.dao.jdbc_implementations.BookDao;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.sortby.BookSortBy;
import com.senla.app.task.repository.StorageRepository;
import com.senla.app.task.model.dto.jdbc.BookDto;

import java.util.ArrayList;
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
        List<String> sortByList = new ArrayList<>();

        switch (sortBy) {
            case TITLE -> sortByList.add("title");
            case PUBLICATION_DATE -> sortByList.add("publication_date");
            case ADMISSION_DATE -> sortByList.add("admission_date");
            case PRICE -> sortByList.add("price");
            case AVAILABILITY -> sortByList.add("books.status");
            case DATE_PRICE -> sortByList.addAll(List.of("admission_date", "price"));
        }

        try {
            return bookDao.findAll(
                    sortBy != BookSortBy.NO_SORT ? sortByList : null
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
