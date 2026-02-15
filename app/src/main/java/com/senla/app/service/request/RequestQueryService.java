package com.senla.app.service.request;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.exceptions.ResourceNotFound;
import com.senla.app.repository.RequestManagerRepository;
import com.senla.app.model.entity.Request;
import com.senla.app.model.entity.sortby.RequestSortBy;
import com.senla.app.service.unit_of_work.UnitOfWork;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RequestQueryService {

    private final RequestManagerRepository requestManagerRepository;

    private final UnitOfWork unitOfWork;

    public RequestQueryService(
            @Db RequestManagerRepository requestManagerRepository,
            @Hibernate UnitOfWork unitOfWork) {
        this.requestManagerRepository = requestManagerRepository;
        this.unitOfWork = unitOfWork;
    }

    @Transactional
    public List<Request> getSorted(RequestSortBy sortBy) {
        List<Request> list = requestManagerRepository.getSortedRequests(sortBy);

        if (list.isEmpty()) {
            throw new ResourceNotFound("запросов нет");
        }

        return list;
    }
}
