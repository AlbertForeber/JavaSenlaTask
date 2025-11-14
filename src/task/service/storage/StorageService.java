package task.service.storage;

import task.model.entity.status.BookStatus;
import task.repository.RequestManagerRepository;
import task.repository.StorageRepository;

public class StorageService {
    private final StorageRepository bookStorageRepository;
    private final RequestManagerRepository requestManagerRepository;

    public StorageService(
            StorageRepository storageRepository,
            RequestManagerRepository requestManagerRepository
    ) {
        this.bookStorageRepository = storageRepository;
        this.requestManagerRepository = requestManagerRepository;
    }

    public void writeOffBook(String bookName) {
        bookStorageRepository.getBook(bookName).setStatus(BookStatus.SOLD_OUT, null);
    }

    public void addBookToStorage(String bookName) {
        bookStorageRepository.getBook(bookName).setStatus(BookStatus.FREE);
        requestManagerRepository.cancelRequests(bookName);
    }
}
