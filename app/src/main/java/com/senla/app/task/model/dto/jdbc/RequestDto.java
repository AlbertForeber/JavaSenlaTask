package com.senla.app.task.model.dto.jdbc;

import com.senla.app.task.model.entity.Request;

public class RequestDto {

    private Integer id = null;
    private final int bookId;
    private final String bookName;
    private int amount;

    public RequestDto(int bookId, String bookName, int amount) {
        this.bookId = bookId;
        this.amount = amount;
        this.bookName = bookName;
    }

    public RequestDto(int id, int bookId, String bookName, int amount) {
        this.id = id;
        this.bookId = bookId;
        this.amount = amount;
        this.bookName = bookName;
    }

    public Integer getId() {
        return id;
    }

    public int getBookId() {
        return bookId;
    }

    public int getAmount() {
        return amount;
    }

    public Request toBusinessObject() {
        return new Request(
                this.id,
                this.bookName,
                this.amount
        );
    }

    public String getBookName() {
        return bookName;
    }
}
