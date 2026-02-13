package com.senla.app.repository.db;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.db.DatabaseException;
import com.senla.app.db.dao.GenericDao;
import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.sortby.BookSortBy;
import com.senla.app.repository.StorageRepository;
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
    public void addBook(Book book) throws DatabaseException {
        bookDao.save(book);
    }

    @Override
    public void updateBook(Book book) throws DatabaseException {
        bookDao.update(book);
    }

    @Override
    public Book getBook(int bookId, boolean getLinkedObjects) throws DatabaseException {
        return bookDao.findById(bookId, getLinkedObjects);
    }

    @Override
    public List<Book> getSortedBooks(BookSortBy sortBy, boolean getLinkedObjects) throws DatabaseException {
        return bookDao.findAll(sortBy != BookSortBy.NO_SORT ? sortBy : null, getLinkedObjects);
    }

    @Override
    public boolean removeBook(int bookId) throws DatabaseException {
        bookDao.delete(bookId);
        return true;
    }
}
