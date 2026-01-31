package com.senla.app.task.service.storage;

import com.senla.annotation.ConfigProperty;
import com.senla.annotation.InjectTo;
import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.task.repository.StorageRepository;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.sortby.BookSortBy;
import com.senla.app.task.model.entity.status.BookStatus;
import com.senla.app.task.repository.db.DbStorageRepository;
import com.senla.app.task.service.unit_of_work.UnitOfWork;
import com.senla.app.task.service.unit_of_work.implementations.HibernateUnitOfWork;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class StorageQueryService {

    @InjectTo(useImplementation = DbStorageRepository.class)
    private StorageRepository bookStorageRepository;

    @ConfigProperty(propertyName = "liquidMonths", type = int.class)
    private final int liquidMonthAmount;

    @InjectTo(useImplementation = HibernateUnitOfWork.class)
    private final UnitOfWork unitOfWork;

    public StorageQueryService(
            @Db                                 StorageRepository bookStorageRepository,
            @Value("${storage.liquid-months}")  int liquidMonthAmount,
            @Hibernate                          UnitOfWork unitOfWork
    ) {
        this.bookStorageRepository = bookStorageRepository;
        this.liquidMonthAmount = liquidMonthAmount;
        this.unitOfWork = unitOfWork;
    }

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
