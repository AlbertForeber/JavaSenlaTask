package com.senla.app.task.repository.db;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.task.db.dao.GenericDao;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.sortby.BookSortBy;
import com.senla.app.task.model.entity.sortby.RequestSortBy;
import com.senla.app.task.repository.StorageRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Db
public class DbStorageRepository implements StorageRepository {

    private final GenericDao<Book, Integer, BookSortBy> bookDao;

    public DbStorageRepository(@Hibernate GenericDao<Book, Integer, BookSortBy> bookDao) {
        this.bookDao = bookDao;
    }

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
