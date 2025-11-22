package task.service.storage;

import task.model.entity.Order;
import task.model.entity.sortby.OrderSortBy;
import task.repository.StorageRepository;
import task.model.entity.Book;
import task.model.entity.sortby.BookSortBy;
import task.model.entity.status.BookStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class StorageQueryService {
    private final StorageRepository bookStorageRepository;
    private final int liquidMonthAmount;

    public StorageQueryService(
            StorageRepository storageRepository,
            int liquidMonthAmount
    ) {
        this.bookStorageRepository = storageRepository;
        this.liquidMonthAmount = liquidMonthAmount;
    }

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
            if (now.get(Calendar.MONTH) - book.getAdmissionDate().get(Calendar.MONTH) >= liquidMonthAmount
                    && book.getStatus() == BookStatus.FREE) {
                toReturn.add(book);
            }
        }

        return List.copyOf(toReturn);
    }

    public String getBookDescription(int bookId) {
        return bookStorageRepository.getBook(bookId).getDescription();
    }

    public void saveState(String path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path + "storage"))) {
            for (Book book : bookStorageRepository.getSortedBooks(BookSortBy.NO_SORT)) {
                oos.writeObject(book);
            }
        }
    }

    public void loadState(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path + "storage"))) {
            Book book;
            while (true) {
                try {
                    book = (Book) ois.readObject();
                    bookStorageRepository.addBook(book);
                } catch (EOFException e) {
                    break;
                }
            }
        }
    }
}
