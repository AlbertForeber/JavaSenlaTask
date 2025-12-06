package com.senla.app.task.service.request;

import com.senla.annotation.InjectTo;
import com.senla.app.task.repository.RequestManagerRepository;
import com.senla.app.task.repository.StorageRepository;
import com.senla.app.task.repository.inmemory.InMemoryRequestManagerRepository;
import com.senla.app.task.repository.inmemory.InMemoryStorageRepository;

public class RequestService {

    @InjectTo(useImplementation = InMemoryRequestManagerRepository.class)
    private RequestManagerRepository requestManagerRepository;

    @InjectTo(useImplementation = InMemoryStorageRepository.class)
    private StorageRepository storageRepository;

    public RequestService() {}

    public void createRequest(int bookId) {
        requestManagerRepository.addRequest(storageRepository.getBook(bookId).getTitle());
    }
}
