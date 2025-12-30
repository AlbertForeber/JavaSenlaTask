package com.senla.app.task.repository;

import com.senla.app.task.model.entity.Request;
import com.senla.app.task.model.entity.sortby.RequestSortBy;


import java.util.List;

public interface RequestManagerRepository {
    void addRequest(int requestId, String bookName);
    void addRequest(String bookName);
    Request getRequest(int requestId);
    void cancelRequests(String bookName);
    List<Request> getRequests();
    List<Request> getSortedRequests(RequestSortBy sortBy);
}
