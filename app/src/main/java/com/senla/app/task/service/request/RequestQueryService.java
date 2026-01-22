package com.senla.app.task.service.request;

import com.senla.annotation.InjectTo;
import com.senla.app.task.repository.RequestManagerRepository;
import com.senla.app.task.model.entity.Request;
import com.senla.app.task.model.entity.sortby.RequestSortBy;
import com.senla.app.task.repository.db.DbRequestManagerRepository;
import com.senla.app.task.service.unit_of_work.UnitOfWork;
import com.senla.app.task.service.unit_of_work.implementations.HibernateUnitOfWork;

import java.io.*;
import java.util.List;

public class RequestQueryService {

    @InjectTo(useImplementation = DbRequestManagerRepository.class)
    private RequestManagerRepository requestManagerRepository;

    @InjectTo(useImplementation = HibernateUnitOfWork.class)
    private UnitOfWork unitOfWork;

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
