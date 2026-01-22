package com.senla.app.task.service.storage;

import com.senla.annotation.ConfigProperty;
import com.senla.annotation.InjectTo;
import com.senla.app.task.repository.StorageRepository;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.sortby.BookSortBy;
import com.senla.app.task.model.entity.status.BookStatus;
import com.senla.app.task.repository.db.DbStorageRepository;
import com.senla.app.task.service.unit_of_work.UnitOfWork;
import com.senla.app.task.service.unit_of_work.implementations.HibernateUnitOfWork;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class StorageQueryService {

    @InjectTo(useImplementation = DbStorageRepository.class)
    private StorageRepository bookStorageRepository;

    @ConfigProperty(propertyName = "liquidMonths", type = int.class)
    private int liquidMonthAmount;

    @InjectTo(useImplementation = HibernateUnitOfWork.class)
    private UnitOfWork unitOfWork;

    public StorageQueryService() { }

    public List<Book> getSorted(BookSortBy sortBy) {
        return unitOfWork.execute(() -> bookStorageRepository.getSortedBooks(sortBy, true));
    }

    public List<Book> getHardToSell(
            int nowYear,
            int nowMonth,
            int nowDate
    ) {
        return unitOfWork.execute(() -> {
            List<Book> books = bookStorageRepository.getSortedBooks(BookSortBy.DATE_PRICE, true);
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
        });
    }


    public String getBookDescription(int bookId) {
        return unitOfWork.execute(() -> bookStorageRepository.getBook(bookId, false).getDescription());
    }
}
