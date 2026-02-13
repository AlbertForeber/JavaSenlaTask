package com.senla.app.model.dto.response;

import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.status.BookStatus;
import java.time.LocalDate;

import static com.senla.app.utils.DateConverter.calendarToLocalDate;

// READ-ONLY`
// Используется только для выдачи ответа пользователю
public class BookResponse {

    private final int articleNumber;
    private final String title;
    private final String description;
    private final LocalDate publicationDate;
    private final LocalDate admissionDate;
    private final int price;
    private final BookStatus status;
    private final int priorityOrderId;

    public BookResponse(Book book) {
        this.articleNumber = book.getId();
        this.title = book.getTitle();
        this.description = book.getDescription();
        this.publicationDate = calendarToLocalDate(book.getPublicationDate());
        this.admissionDate = calendarToLocalDate(book.getAdmissionDate());
        this.price = book.getPrice();
        this.status = book.getStatus();
        this.priorityOrderId = book.getOrderId();
    }

    public int getArticleNumber() {
        return articleNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public int getPrice() {
        return price;
    }

    public BookStatus getStatus() {
        return status;
    }

    public int getPriorityOrderId() {
        return priorityOrderId;
    }
}
