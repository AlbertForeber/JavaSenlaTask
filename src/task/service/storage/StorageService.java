package task.service.storage;

import task.model.entity.Book;
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

    public void writeOffBook(int bookId) {
        bookStorageRepository.getBook(bookId).setStatus(BookStatus.SOLD_OUT, null);
    }

    public void addBookToStorage(int bookId) {
        Book book = bookStorageRepository.getBook(bookId);
        book.setStatus(BookStatus.FREE);
        requestManagerRepository.cancelRequests(book.getTitle());
    }
}
