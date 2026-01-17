package com.senla.app.task.model.dto.hibernate;

import com.senla.app.task.model.entity.status.BookStatus;
import jakarta.persistence.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class BookDto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_seq")
    @SequenceGenerator(
            name = "books_seq",
            sequenceName = "books_id_seq",
            allocationSize = 1 // Сколько держим объектов в памяти, до например, операции вставки
    )
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.DATE)
    @Column(name = "publication_date")
    private Calendar publicationDate = new GregorianCalendar();

    @Temporal(TemporalType.DATE)
    @Column(name = "admission_date")
    private Calendar admissionDate = new GregorianCalendar();

    @Column(name = "price")
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookStatus status;

    @ManyToOne
    @JoinColumn(referencedColumnName = "order_id")
    private OrderDto orderDto;
}