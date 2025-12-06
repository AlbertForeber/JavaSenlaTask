package com.senla.app.task.service.storage;

import com.senla.annotation.ConfigProperty;
import com.senla.annotation.InjectTo;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.status.BookStatus;
import com.senla.app.task.repository.RequestManagerRepository;
import com.senla.app.task.repository.StorageRepository;
import com.senla.app.task.repository.inmemory.InMemoryRequestManagerRepository;
import com.senla.app.task.repository.inmemory.InMemoryStorageRepository;

public class StorageService {
    @InjectTo(useImplementation = InMemoryStorageRepository.class)
    private StorageRepository bookStorageRepository;

    @InjectTo(useImplementation = InMemoryRequestManagerRepository.class)
    private RequestManagerRepository requestManagerRepository;

    @ConfigProperty(propertyName="cancelRequests", type=boolean.class)
    private boolean cancelRequests = true;

    public StorageService() {}

    public void writeOffBook(int bookId) {
        bookStorageRepository.getBook(bookId).setStatus(BookStatus.SOLD_OUT, null);
    }

    public void addBookToStorage(int bookId) {
        Book book = bookStorageRepository.getBook(bookId);
        book.setStatus(BookStatus.FREE);

        if (cancelRequests)
            requestManagerRepository.cancelRequests(book.getTitle());
    }
}
