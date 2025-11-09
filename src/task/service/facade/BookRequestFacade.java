package task.service.facade;

import task.model.entity.Request;
import task.model.entity.sortby.RequestSortBy;
import task.service.domain.RequestManagerService;

public class BookRequestFacade {
    private final RequestManagerService requestManagerService;

    public BookRequestFacade(
            RequestManagerService requestManagerService
    ) {
        this.requestManagerService = requestManagerService;
    }

    public void createRequest(String bookName) {
        Request request = new Request(bookName);
        requestManagerService.addRequest(request);
    }

    public Request[] getSorted(RequestSortBy sortBy) {
        return requestManagerService.getSortedRequests(sortBy);
    }

}
