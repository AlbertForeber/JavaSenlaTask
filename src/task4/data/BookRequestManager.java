package task4.data;

import task4.data.dto.Request;
import task4.domain.RequestManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookRequestManager implements RequestManager {
    private final List<Request> requests = new ArrayList<>();

    @Override
    public void addRequest(Request request) {
        requests.add(request);
    }

    @Override
    public void cancelRequests(String bookName) {
        requests.removeIf(r -> Objects.equals(r.getBookName(), bookName));
    }

    @Override
    public List<Request> getRequests() {
        return requests;
    }

    @Override
    public String toString() {
        return requests.toString();
    }

    public List<Request> getDebugRequest() {
        return requests;
    }
}
