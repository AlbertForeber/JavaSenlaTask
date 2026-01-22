package com.senla.app.task.service.request;

import com.senla.annotation.InjectTo;
import com.senla.app.task.repository.RequestManagerRepository;
import com.senla.app.task.repository.StorageRepository;
import com.senla.app.task.repository.db.DbRequestManagerRepository;
import com.senla.app.task.repository.db.DbStorageRepository;
import com.senla.app.task.service.unit_of_work.UnitOfWork;
import com.senla.app.task.service.unit_of_work.implementations.HibernateUnitOfWork;

public class RequestService {

    @InjectTo(useImplementation = DbRequestManagerRepository.class)
    private RequestManagerRepository requestManagerRepository;

    @InjectTo(useImplementation = DbStorageRepository.class)
    private StorageRepository storageRepository;

    @InjectTo(useImplementation = HibernateUnitOfWork.class)
    private UnitOfWork unitOfWork;

    public RequestService() { }

    public void createRequest(int bookId) {
        unitOfWork.executeVoid(() -> requestManagerRepository
                        .addRequest(storageRepository
                        .getBook(bookId, false)));
    }
}
