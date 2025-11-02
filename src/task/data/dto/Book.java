package task4.data.dto;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Book {
    private final String title;
    private final String description;
    private final Calendar publicationDate = new GregorianCalendar();
    private final Calendar admissionDate = new GregorianCalendar();
    private int price;
    private BookStatus status;

    public Book(
            String title,
            String description,
            int year,
            int month,
            int date,
            BookStatus initStatus
    ) {
        this.title = title;
        this.description = description;
        this.admissionDate.set(year, month, date);
        this.status = initStatus;
    }

    public String getTitle() {
        return title;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public Calendar getDate() {
        return publicationDate;
    }

    public void setDate(int year, int month, int date) {
        this.publicationDate.set(year, month, date);
    }

    public Calendar getAdmissionDate() {
        return admissionDate;
    }

    public String getDescription() {
        return description;
    }

    public Calendar getPublicationDate() {
        return publicationDate;
    }

    @Override
    public String toString() {
        return String.format("""
                BOOK:
                title: %s
                description: %s
                publicationDate: %d %d %d
                admissionDate: %d %d %d
                price: %d
                bookStatus: %s
                """,
                title,
                description,
                publicationDate.get(Calendar.YEAR),
                publicationDate.get(Calendar.MONTH),
                publicationDate.get(Calendar.DATE),
                admissionDate.get(Calendar.YEAR),
                admissionDate.get(Calendar.MONTH),
                admissionDate.get(Calendar.DATE),
                price,
                status
                );
    }
}
