package com.senla.app.service.storage;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.exceptions.ResourceNotFound;
import com.senla.app.exceptions.WrongId;
import com.senla.app.repository.StorageRepository;
import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.sortby.BookSortBy;
import com.senla.app.model.entity.status.BookStatus;
import com.senla.app.service.unit_of_work.UnitOfWork;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class StorageQueryService {

    private final StorageRepository bookStorageRepository;
    private final int liquidMonthAmount;

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

    @Transactional
    public List<Book> getSorted(BookSortBy sortBy) {
        List<Book> books = bookStorageRepository.getSortedBooks(sortBy, true);

        if (books.isEmpty()) throw new ResourceNotFound("книг нет");
        return books;
    }

    @Transactional
    public List<Book> getHardToSell(
            int nowYear,
            int nowMonth,
            int nowDate
    ) {
        List<Book> books = bookStorageRepository.getSortedBooks(BookSortBy.DATE_PRICE, true);
        if (books.isEmpty()) throw new ResourceNotFound("книг нет");

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

    @Transactional
    public String getBookDescription(int bookId) {
        Book book = bookStorageRepository.getBook(bookId, false);

        if (book == null) throw new WrongId("книги с артикулом #" + bookId + " не существует");
        return book.getDescription();
    }
}
