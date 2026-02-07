package com.senla.app.service.request;

import com.senla.annotation.InjectTo;
import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.repository.RequestManagerRepository;
import com.senla.app.model.entity.Request;
import com.senla.app.model.entity.sortby.RequestSortBy;
import com.senla.app.repository.db.DbRequestManagerRepository;
import com.senla.app.service.unit_of_work.UnitOfWork;
import com.senla.app.service.unit_of_work.implementations.HibernateUnitOfWork;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class RequestQueryService {

    @InjectTo(useImplementation = DbRequestManagerRepository.class)
    private final RequestManagerRepository requestManagerRepository;

    @InjectTo(useImplementation = HibernateUnitOfWork.class)
    private final UnitOfWork unitOfWork;

    public RequestQueryService(
            @Db RequestManagerRepository requestManagerRepository,
            @Hibernate UnitOfWork unitOfWork) {
        this.requestManagerRepository = requestManagerRepository;
        this.unitOfWork = unitOfWork;
    }

    public List<Request> getSorted(RequestSortBy sortBy) {
        return unitOfWork.execute(() -> requestManagerRepository.getSortedRequests(sortBy));
    }

    public void saveState(String path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path + "request"))) {
            for (Request request : requestManagerRepository.getSortedRequests(RequestSortBy.NO_SORT)) {
                oos.writeObject(request);
            }
        }
    }
}
