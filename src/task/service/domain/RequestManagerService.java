package task.service.domain;

import task.model.entity.Request;
import task.model.entity.sortby.RequestSortBy;


import java.util.List;

public interface RequestManager {
    void addRequest(Request request);
    void cancelRequests(String bookName);
    List<Request> getRequests();
    Request[] getSortedRequests(RequestSortBy sortBy);
}
