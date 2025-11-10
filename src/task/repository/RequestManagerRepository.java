package task.repository;

import task.model.entity.Request;
import task.model.entity.sortby.RequestSortBy;


import java.util.List;

public interface RequestManagerRepository {
    void addRequest(Request request);
    void cancelRequests(String bookName);
    List<Request> getRequests();
    List<Request> getSortedRequests(RequestSortBy sortBy);
}
