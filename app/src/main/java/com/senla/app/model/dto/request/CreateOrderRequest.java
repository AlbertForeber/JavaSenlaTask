package com.senla.app.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateOrderRequest {

    @NotNull(message = "Заказ должен содержать идентификатор")
    private int id;

    @NotEmpty(message = "В заказе должен быть артикул хотя бы одной книги")
    private List<Integer> orderedBooksNumbers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getOrderedBooksNumbers() {
        return orderedBooksNumbers;
    }

    public void setOrderedBookNumbers(List<Integer> orderedBooksNumbers) {
        this.orderedBooksNumbers = orderedBooksNumbers;
    }
}
