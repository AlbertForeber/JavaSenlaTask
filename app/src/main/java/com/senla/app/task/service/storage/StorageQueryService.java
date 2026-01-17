package com.senla.app.task.service.storage;

import com.senla.annotation.ConfigProperty;
import com.senla.annotation.InjectTo;
import com.senla.app.task.repository.StorageRepository;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.sortby.BookSortBy;
import com.senla.app.task.model.entity.status.BookStatus;
import com.senla.app.task.repository.db.DbStorageRepository;
import com.senla.app.task.model.dto.jdbc.BookDto;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class StorageQueryService {

    @InjectTo(useImplementation = DbStorageRepository.class)
    private StorageRepository bookStorageRepository;

    @ConfigProperty(propertyName = "liquidMonths", type = int.class)
    private int liquidMonthAmount = 6;

    public StorageQueryService() { }

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

        Calendar now = new GregorianCalendar(nowYear, nowMonth - 1, nowDate);

        for (Book book : books) {

            int passedMonths = (now.get(Calendar.MONTH) - book.getAdmissionDate().get(Calendar.MONTH)) +
                    (now.get(Calendar.YEAR) - book.getAdmissionDate().get(Calendar.YEAR)) * 12;

            if (passedMonths >= liquidMonthAmount
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
                    bookStorageRepository.addBook(new BookDto(book, null));
                } catch (EOFException e) {
                    break;
                }
            }
        }
    }
}
