package task.domain;

import task.data.dto.Request;
import task.data.dto.sortby.RequestSortBy;


import java.util.List;

public interface RequestManager {
    void addRequest(Request request);
    void cancelRequests(String bookName);
    List<Request> getRequests();
    Request[] getSortedRequests(RequestSortBy sortBy);
}
