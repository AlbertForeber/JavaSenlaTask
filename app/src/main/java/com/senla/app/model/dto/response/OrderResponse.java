package com.senla.app.model.dto.response;

import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.Order;
import com.senla.app.model.entity.status.OrderStatus;

import java.time.LocalDate;
import java.util.List;

import static com.senla.app.utils.DateConverter.calendarToLocalDate;

// READ-ONLY
// Используется только для выдачи ответа пользователю
public class OrderResponse {

    private final int id;
    private final List<Integer> orderedBooksNumbers;
    private final String customerName;
    private final int totalSum;
    private final LocalDate completionDate;
    private final OrderStatus status;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.orderedBooksNumbers = order.getOrderedBooks().stream().map(Book::getId).toList();
        this.customerName = order.getCustomerName();
        this.totalSum = order.getTotalSum();
        this.completionDate = calendarToLocalDate(order.getCompletionDate());
        this.status = order.getStatus();
    }

    public int getId() {
        return id;
    }

    public List<Integer> getOrderedBooksNumbers() {
        return orderedBooksNumbers;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getTotalSum() {
        return totalSum;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
