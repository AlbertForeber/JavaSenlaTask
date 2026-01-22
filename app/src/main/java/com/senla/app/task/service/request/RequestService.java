package com.senla.app.task.service.request;

import com.senla.annotation.InjectTo;
import com.senla.app.task.repository.RequestManagerRepository;
import com.senla.app.task.repository.StorageRepository;
import com.senla.app.task.repository.db.DbRequestManagerRepository;
import com.senla.app.task.repository.db.DbStorageRepository;

public class RequestService {

    @InjectTo(useImplementation = DbRequestManagerRepository.class)
    private RequestManagerRepository requestManagerRepository;

    @InjectTo(useImplementation = DbStorageRepository.class)
    private StorageRepository storageRepository;

    public RequestService() { }

    public void createRequest(int bookId) {
        requestManagerRepository.addRequest(storageRepository.getBook(bookId));
    }
}
