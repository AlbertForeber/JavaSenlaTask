package com.senla.app.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateOrderRequest {

    @NotNull(message = "Заказ должен содержать идентификатор")
    private Integer id;

    @NotEmpty(message = "В заказе должен быть артикул хотя бы одной книги")
    private List<Integer> orderedBooksNumbers;

    @NotBlank(message = "Имя заказчика не должно быть пустым")
    private String customerName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getOrderedBooksNumbers() {
        return orderedBooksNumbers;
    }

    public void setOrderedBookNumbers(List<Integer> orderedBooksNumbers) {
        this.orderedBooksNumbers = orderedBooksNumbers;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
