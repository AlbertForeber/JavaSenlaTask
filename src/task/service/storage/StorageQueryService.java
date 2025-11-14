package task.service.storage;

import task.repository.StorageRepository;
import task.model.entity.Book;
import task.model.entity.sortby.BookSortBy;
import task.model.entity.status.BookStatus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class StorageQueryService {
    private final StorageRepository bookStorageRepository;

    public StorageQueryService(
            StorageRepository storageRepository
    ) {
        this.bookStorageRepository = storageRepository;}

    public List<Book> getSorted(BookSortBy sortBy) {
        return bookStorageRepository.getSortedBooks(sortBy);
    }

    public List<Book> getHardToSell(
            int nowYear,
            int nowMonth,
            int nowDate
    ) {
        List<Book> books = bookStorageRepository.getSortedBooks(BookSortBy.DATE_PRICE);
        ArrayList<Book> toReturn = new ArrayList<>();

        Calendar now = new GregorianCalendar(nowYear, nowMonth, nowDate);

        for (Book book : books) {
            if (now.get(Calendar.MONTH) - book.getAdmissionDate().get(Calendar.MONTH) >= 6
                    && book.getStatus() == BookStatus.FREE) {
                toReturn.add(book);
            }
        }

        return List.copyOf(toReturn);
    }

    public String getBookDescription(String bookName) {
        return bookStorageRepository.getBook(bookName).getDescription();
    }
}
