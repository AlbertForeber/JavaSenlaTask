package task.service.request;

import task.repository.RequestManagerRepository;
import task.model.entity.Request;
import task.model.entity.sortby.RequestSortBy;

import java.util.List;

public class RequestQueryService {
    private final RequestManagerRepository requestManagerRepository;

    public RequestQueryService(
            RequestManagerRepository requestManagerRepository
    ) {
        this.requestManagerRepository = requestManagerRepository;
    }

    public List<Request> getSorted(RequestSortBy sortBy) {
        return requestManagerRepository.getSortedRequests(sortBy);
    }

}
