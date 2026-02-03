package com.senla.app.task.model.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity(name = "requests")
public class Request implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "request_seq")
    @SequenceGenerator(name = "request_seq", sequenceName = "requests_id_seq", allocationSize = 1)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @Column(name = "amount")
    private int amount = 1;

    protected Request() { }

    public Request(int id, Book book) {
        this.id = id;
        this.book = book;
    }

    public Request(int id, Book book, int amount) {
        this.id = id;
        this.book = book;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void incrementAmount() {
        this.amount++;
    }

    public int getAmount() {
        return amount;
    }

    public Book getBook() {
        return book;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Request) {
            return this.book.equals(((Request) obj).book);
        } else return false;
    }

    @Override
    public int hashCode() {
        return this.book.hashCode();
    }

    @Override
    public String toString() {
        return String.format("""
                REQUEST:
                    id: %d,
                    bookName: %s,
                    amount: %d
                """,
                id,
                book.getTitle(),
                amount
        );
    }
}
