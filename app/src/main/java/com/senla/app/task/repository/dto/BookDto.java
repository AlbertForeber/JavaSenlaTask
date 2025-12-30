package com.senla.app.task.repository.dto;

import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.status.BookStatus;
import com.senla.app.task.utils.DataConverter;

import java.time.LocalDate;

public class BookDto implements Dto<Book> {
    private final int id;
    private final String title;
    private final String description;
    private final LocalDate publicationDate;
    private final LocalDate admissionDate;
    private int price;
    private String status;
    private String reservist = null;
    private Integer orderId = null;

    public BookDto(Book book, Integer orderId) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.description = book.getDescription();
        this.publicationDate = DataConverter.calendarToLocalDate(book.getPublicationDate());
        this.admissionDate = DataConverter.calendarToLocalDate(book.getAdmissionDate());
        this.price = book.getPrice();
        this.status = book.getStatus().name();
        this.reservist = book.getReservist();
        this.orderId = orderId;
    }

    public BookDto(
            int id,
            String title,
            String description,
            LocalDate publicationDate,
            LocalDate admissionDate,
            int price,
            String status,
            String reservist,
            Integer orderId
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
        this.admissionDate = admissionDate;
        this.price = price;
        this.status = status;
        this.reservist = reservist;
        this.orderId = orderId;
    }

    private BookStatus convertStatus(String status) {
        return switch (status) {
            case "RESERVED" -> BookStatus.RESERVED;
            case "SOLD_OUT" -> BookStatus.SOLD_OUT;
            default -> BookStatus.FREE;
        };
    }

    @Override
    public Book toBusinessObject() {
        return new Book(
                this.id,
                this.title,
                this.description,
                DataConverter.localDateToCalendar(this.publicationDate),
                DataConverter.localDateToCalendar(this.admissionDate),
                this.price,
                this.convertStatus(this.status),
                this.reservist
        );
    }

    public Integer getOrderId() {
        return orderId;
    }

    public String getReservist() {
        return reservist;
    }

    public String getStatus() {
        return status;
    }

    public int getPrice() {
        return price;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

}
