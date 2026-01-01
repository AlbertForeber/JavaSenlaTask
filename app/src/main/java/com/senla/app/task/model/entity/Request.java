package com.senla.app.task.model.entity;

import java.io.Serializable;

public class Request implements Serializable {
    private final int id;
    private final String bookName;
    private int amount = 1;

    public Request(int id, String bookName) {
        this.id = id;
        this.bookName = bookName;
    }

    public Request(int id, String bookName, int amount) {
        this.id = id;
        this.bookName = bookName;
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

    public String getBookName() {
        return bookName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Request) {
            return this.bookName.equals(((Request) obj).bookName);
        } else return false;
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
                bookName,
                amount
        );
    }


}
