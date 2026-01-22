package com.senla.app.task.repository;

import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.Request;
import com.senla.app.task.model.entity.sortby.RequestSortBy;


import java.util.List;

public interface RequestManagerRepository {

    void addRequest(int requestId, Book book);
    void addRequest(Book book);
    Request getRequest(int requestId); // Подгрузку книги опциональной не делаем, т.к. сильно связана
    void cancelRequests(String bookName);
    List<Request> getSortedRequests(RequestSortBy sortBy);
}
