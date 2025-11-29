package com.senla.app.task.service.request;

import com.senla.annotation.InjectTo;
import com.senla.app.task.repository.RequestManagerRepository;
import com.senla.app.task.repository.StorageRepository;

public class RequestService {

    @InjectTo
    private RequestManagerRepository requestManagerRepository;

    @InjectTo
    private StorageRepository storageRepository;

    public RequestService() {}

    public void createRequest(int bookId) {
        requestManagerRepository.addRequest(storageRepository.getBook(bookId).getTitle());
    }
}
