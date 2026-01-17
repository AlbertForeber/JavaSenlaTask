package com.senla.app.task.model.dto;

import com.senla.app.task.model.entity.Order;
import com.senla.app.task.model.entity.status.BookStatus;
import com.senla.app.task.model.entity.status.OrderStatus;
import com.senla.app.task.utils.DataConverter;

import java.time.LocalDate;
import java.util.List;

public class OrderDto {
    private final int id;
    private final String customerName;
    private final int totalSum;
    private LocalDate completionDate = null;
    private final String status;

    public OrderDto(int id, String customerName, int totalSum, LocalDate completionDate, String status) {
        this.id = id;
        this.customerName = customerName;
        this.totalSum = totalSum;
        this.completionDate = completionDate;
        this.status = status;
    }

    public OrderDto(Order order) {
        this.id = order.getId();
        this.customerName = order.getCustomerName();
        this.totalSum = order.getTotalSum();
        this.completionDate = order.getCompletionDate() != null ? DataConverter.calendarToLocalDate(order.getCompletionDate()) : null;
        this.status = order.getStatus().name();
    }

    public Order toBusinessObject(List<Integer> orderedBookIds) {
        return new Order(
                this.id,
                orderedBookIds,
                this.customerName,
                this.totalSum,
                this.completionDate != null ? DataConverter.localDateToCalendar(this.completionDate) : null,
                convertStatus(this.status)
        );
    }

    private OrderStatus convertStatus(String status) {
        return switch (status) {
            case "CANCELED" -> OrderStatus.CANCELED;
            case "COMPLETED" -> OrderStatus.COMPLETED;
            default -> OrderStatus.NEW;
        };
    }

    public int getId() {
        return id;
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

    public String getStatus() {
        return status;
    }
}
