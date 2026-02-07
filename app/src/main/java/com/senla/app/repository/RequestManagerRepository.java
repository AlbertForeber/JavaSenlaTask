package com.senla.app.repository;

import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.Request;
import com.senla.app.model.entity.sortby.RequestSortBy;


import java.util.List;

public interface RequestManagerRepository {

    void addRequest(int requestId, Book book);
    void addRequest(Book book);
    Request getRequest(int requestId); // Подгрузку книги опциональной не делаем, т.к. сильно связана
    void cancelRequests(String bookName);
    List<Request> getSortedRequests(RequestSortBy sortBy);
}
