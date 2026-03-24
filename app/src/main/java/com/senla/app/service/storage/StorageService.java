package com.senla.app.service.storage;

import com.senla.annotation.ConfigProperty;
import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.exceptions.WrongId;
import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.status.BookStatus;
import com.senla.app.repository.RequestManagerRepository;
import com.senla.app.repository.StorageRepository;
import com.senla.app.service.unit_of_work.UnitOfWork;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StorageService {

    private final StorageRepository bookStorageRepository;

    private final RequestManagerRepository requestManagerRepository;

    private final UnitOfWork unitOfWork;

    @ConfigProperty(propertyName = "cancelRequests", type = boolean.class)
    private final boolean cancelRequests;

    public StorageService(
            @Db                                     StorageRepository bookStorageRepository,
            @Db                                     RequestManagerRepository requestManagerRepository,
            @Hibernate                              UnitOfWork unitOfWork,
            @Value("${storage.cancel-requests}")                          boolean cancelRequests) {
        this.bookStorageRepository = bookStorageRepository;
        this.requestManagerRepository = requestManagerRepository;
        this.unitOfWork = unitOfWork;
        this.cancelRequests = cancelRequests;
    }

    @Transactional
    public Book writeOffBook(int bookId) {
        Book book = bookStorageRepository.getBook(bookId, false);
        if (book == null) throw new WrongId("книги с артикулом #" + bookId + " не существует");

        book.setStatus(BookStatus.SOLD_OUT, null);
        bookStorageRepository.updateBook(book);
        return book;
    }

    @Transactional
    public Book addBookToStorage(int bookId) {
        Book book = bookStorageRepository.getBook(bookId, false);
        if (book == null) throw new WrongId("книги с артикулом #" + bookId + " не существует");

        book.setStatus(BookStatus.FREE);

        bookStorageRepository.updateBook(book);

        if (cancelRequests)
            requestManagerRepository.cancelRequests(book.getTitle());
        return book;
    }
}
