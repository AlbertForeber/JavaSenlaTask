package task.service.request;

import task.model.entity.Request;
import task.repository.RequestManagerRepository;
import task.repository.StorageRepository;

public class RequestService {
    private final RequestManagerRepository requestManagerRepository;
    private final StorageRepository storageRepository;

    public RequestService(
            RequestManagerRepository requestManagerRepository,
            StorageRepository storageRepository
    ) {
        this.requestManagerRepository = requestManagerRepository;
        this.storageRepository = storageRepository;
    }

    public void createRequest(int bookId) {
        requestManagerRepository.addRequest(storageRepository.getBook(bookId).getTitle());
    }
}
