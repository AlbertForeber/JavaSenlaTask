package com.senla.app.task.service.storage;

import com.senla.annotation.ConfigProperty;
import com.senla.annotation.InjectTo;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.status.BookStatus;
import com.senla.app.task.repository.RequestManagerRepository;
import com.senla.app.task.repository.StorageRepository;
import com.senla.app.task.repository.db.DbStorageRepository;
import com.senla.app.task.repository.db.DbRequestManagerRepository;
import com.senla.app.task.service.unit_of_work.UnitOfWork;
import com.senla.app.task.service.unit_of_work.implementations.HibernateUnitOfWork;

public class StorageService {

    @InjectTo(useImplementation = DbStorageRepository.class)
    private StorageRepository bookStorageRepository;

    @InjectTo(useImplementation = DbRequestManagerRepository.class)
    private RequestManagerRepository requestManagerRepository;

    @InjectTo(useImplementation = HibernateUnitOfWork.class)
    private UnitOfWork unitOfWork;

    @ConfigProperty(propertyName = "cancelRequests", type = boolean.class)
    private boolean cancelRequests = true;

    public StorageService() { }

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
