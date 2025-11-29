package task.service.storage;

import task.model.entity.Book;
import task.model.entity.status.BookStatus;
import task.repository.RequestManagerRepository;
import task.repository.StorageRepository;

public class StorageService {
    private final StorageRepository bookStorageRepository;
    private final RequestManagerRepository requestManagerRepository;


    private boolean cancelRequests;

    public StorageService(
            StorageRepository storageRepository,
            RequestManagerRepository requestManagerRepository,
            boolean cancelRequests
    ) {
        this.bookStorageRepository = storageRepository;
        this.requestManagerRepository = requestManagerRepository;
        this.cancelRequests = cancelRequests;
    }

    public void writeOffBook(int bookId) {
        bookStorageRepository.getBook(bookId).setStatus(BookStatus.SOLD_OUT, null);
    }

    public void addBookToStorage(int bookId) {
        Book book = bookStorageRepository.getBook(bookId);
        book.setStatus(BookStatus.FREE);

        if (cancelRequests)
            requestManagerRepository.cancelRequests(book.getTitle());
    }
}
