package com.senla.app.task.service.request;

import com.senla.annotation.InjectTo;
import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.task.repository.RequestManagerRepository;
import com.senla.app.task.repository.StorageRepository;
import com.senla.app.task.repository.db.DbRequestManagerRepository;
import com.senla.app.task.repository.db.DbStorageRepository;
import com.senla.app.task.service.unit_of_work.UnitOfWork;
import com.senla.app.task.service.unit_of_work.implementations.HibernateUnitOfWork;
import org.springframework.stereotype.Service;

@Service
public class RequestService {

    @InjectTo(useImplementation = DbRequestManagerRepository.class)
    private final RequestManagerRepository requestManagerRepository;

    @InjectTo(useImplementation = DbStorageRepository.class)
    private final StorageRepository storageRepository;

    @InjectTo(useImplementation = HibernateUnitOfWork.class)
    private final UnitOfWork unitOfWork;

    public RequestService(
            @Db RequestManagerRepository requestManagerRepository,
            @Db StorageRepository storageRepository,
            @Hibernate UnitOfWork unitOfWork) {
        this.requestManagerRepository = requestManagerRepository;
        this.storageRepository = storageRepository;
        this.unitOfWork = unitOfWork;
    }

    public void createRequest(int bookId) {
        unitOfWork.executeVoid(() -> requestManagerRepository
                        .addRequest(storageRepository
                        .getBook(bookId, false)));
    }
}
