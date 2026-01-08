package com.senla.app.task.model.entity;

import com.senla.app.task.model.entity.status.BookStatus;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class Book implements Serializable {

    private final int id;
    private final String title;
    private final String description;
    private Calendar publicationDate = new GregorianCalendar();
    private Calendar admissionDate = new GregorianCalendar();
    private int price;
    private BookStatus status;
    private String reservist = null;

    public Book(
            int id,
            String title,
            String description,
            int year,
            int month,
            int date,
            BookStatus initStatus
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.admissionDate.set(year, month - 1, date);
        this.status = initStatus;

        // Случайное заполнение даты публикации и цены
        Random random = new Random();

        setPublicationDate(
                random.nextInt(1885, 2000),
                random.nextInt(0, 12),
                random.nextInt(1, 29)
        );

        setPrice(random.nextInt(100, 1000));
    }

    public Book(
            int id,
            String title,
            String description,
            Calendar publicationDate,
            Calendar admissionDate,
            int price,
            BookStatus initStatus,
            String reservist
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
        this.admissionDate = admissionDate;
        this.price = price;
        this.status = initStatus;
        this.reservist = reservist;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        if (status != BookStatus.RESERVED) {
            this.status = status;
            this.reservist = null;
        }
    }

    public void setStatus(BookStatus status, String reservist) {
        if (status == BookStatus.RESERVED) {
            this.reservist = reservist;
        }
        this.status = status;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPublicationDate(int year, int month, int date) {
        this.publicationDate.set(year, month - 1, date);
    }

    public Calendar getPublicationDate() {
        return publicationDate;
    }

    public Calendar getAdmissionDate() {
        return admissionDate;
    }

    public String getDescription() {
        return description;
    }

    public String getReservist() {
        return reservist;
    }

    @Override
    public String toString() {
        return String.format("""
                BOOK:
                    id: %d
                    title: %s
                    description: %s
                    publicationDate: %d %d %d
                    admissionDate: %d %d %d
                    price: %d
                    bookStatus: %s
                """,
                id,
                title,
                description,
                publicationDate.get(Calendar.YEAR),
                publicationDate.get(Calendar.MONTH) + 1,
                publicationDate.get(Calendar.DATE),
                admissionDate.get(Calendar.YEAR),
                admissionDate.get(Calendar.MONTH) + 1,
                admissionDate.get(Calendar.DATE),
                price,
                status
                );
    }
}
