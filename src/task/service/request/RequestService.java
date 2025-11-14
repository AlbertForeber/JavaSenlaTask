package task.service.request;

import task.model.entity.Request;
import task.repository.RequestManagerRepository;

public class RequestService {
    private final RequestManagerRepository requestManagerRepository;

    public RequestService(
            RequestManagerRepository requestManagerRepository
    ) {
        this.requestManagerRepository = requestManagerRepository;
    }

    public void createRequest(String bookName) {
        Request request = new Request(bookName);
        requestManagerRepository.addRequest(request);
    }
}
