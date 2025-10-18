package task4.domain;

import task4.data.dto.Request;

import java.util.List;

public interface RequestManager {
    void addRequest(Request request);
    void cancelRequests(String bookName);
    List<Request> getRequests();
}
