package task.service.facade;

import task.model.entity.Book;
import task.model.entity.sortby.BookSortBy;
import task.model.entity.status.BookStatus;
import task.service.domain.RequestManagerService;
import task.service.domain.StorageService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class BookStorageFacade {
    private final StorageService bookStorageService;
    private final RequestManagerService requestManagerService;

    public BookStorageFacade(
            StorageService storageService,
            RequestManagerService requestManagerService
    ) {
        this.bookStorageService = storageService;
        this.requestManagerService = requestManagerService;
    }

    public void writeOffBook(String bookName) {
        bookStorageService.getBook(bookName).setStatus(BookStatus.SOLD_OUT, null);
    }

    public void addBookToStorage(String bookName) {
        bookStorageService.getBook(bookName).setStatus(BookStatus.FREE);
        requestManagerService.cancelRequests(bookName);
    }

    public Book[] getSorted(BookSortBy sortBy) {
        return bookStorageService.getSortedBooks(sortBy);
    }

    public Book[] getHardToSell(
            int nowYear,
            int nowMonth,
            int nowDate
    ) {
        Book[] books = bookStorageService.getSortedBooks(BookSortBy.DATE_PRICE);
        ArrayList<Book> toReturn = new ArrayList<>();

        Calendar now = new GregorianCalendar(nowYear, nowMonth, nowDate);

        for (Book book : books) {
            if (now.get(Calendar.MONTH) - book.getAdmissionDate().get(Calendar.MONTH) >= 6
                    && book.getStatus() == BookStatus.FREE) {
                toReturn.add(book);
            }
        }

        return toReturn.toArray(new Book[0]);
    }

    public String getBookDescription(String bookName) {
        return bookStorageService.getBook(bookName).getDescription();
    }

}
