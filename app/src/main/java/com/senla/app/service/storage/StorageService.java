package com.senla.app.service.storage;

import com.senla.annotation.ConfigProperty;
import com.senla.annotation.InjectTo;
import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.status.BookStatus;
import com.senla.app.repository.RequestManagerRepository;
import com.senla.app.repository.StorageRepository;
import com.senla.app.repository.db.DbStorageRepository;
import com.senla.app.repository.db.DbRequestManagerRepository;
import com.senla.app.service.unit_of_work.UnitOfWork;
import com.senla.app.service.unit_of_work.implementations.HibernateUnitOfWork;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StorageService {

    @InjectTo(useImplementation = DbStorageRepository.class)
    private final StorageRepository bookStorageRepository;

    @InjectTo(useImplementation = DbRequestManagerRepository.class)
    private final RequestManagerRepository requestManagerRepository;

    @InjectTo(useImplementation = HibernateUnitOfWork.class)
    private final UnitOfWork unitOfWork;

    @ConfigProperty(propertyName = "cancelRequests", type = boolean.class)
    private final boolean cancelRequests;

    public StorageService(
            @Db                                     StorageRepository bookStorageRepository,
            @Db                                     RequestManagerRepository requestManagerRepository,
            @Hibernate                              UnitOfWork unitOfWork,
            @Value("${storage.cancel-requests}")    boolean cancelRequests) {
        this.bookStorageRepository = bookStorageRepository;
        this.requestManagerRepository = requestManagerRepository;
        this.unitOfWork = unitOfWork;
        this.cancelRequests = cancelRequests;
    }

    public void writeOffBook(int bookId) {
        unitOfWork.executeVoid(() -> {
            Book book = bookStorageRepository.getBook(bookId, false);
            book.setStatus(BookStatus.SOLD_OUT, null);

            bookStorageRepository.updateBook(book);
        });
    }

    public void addBookToStorage(int bookId) {
        unitOfWork.executeVoid(() -> {
            Book book = bookStorageRepository.getBook(bookId, false);
            book.setStatus(BookStatus.FREE);

            bookStorageRepository.updateBook(book);

            if (cancelRequests)
                requestManagerRepository.cancelRequests(book.getTitle());
        });
    }
}
