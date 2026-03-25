package com.senla.app.model.dto.response;

import com.senla.app.model.entity.Request;

public class RequestResponse {

    private final int id;
    private final String bookName;
    private final int amount;

    public RequestResponse(Request request) {
        this.id = request.getId();
        this.bookName = request.getBook().getTitle();
        this.amount = request.getAmount();
    }

    public int getId() {
        return id;
    }

    public String getBookName() {
        return bookName;
    }

    public int getAmount() {
        return amount;
    }
}
