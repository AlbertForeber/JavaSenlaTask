package com.senla.app.task.repository;

import com.senla.app.task.model.entity.Order;
import com.senla.app.task.model.entity.sortby.OrderSortBy;

import java.util.List;

public interface OrderManagerRepository {

    void addOrder(int orderId, Order order) throws IllegalArgumentException;
    void updateOrder(Order order);
    Order getOrder(int orderId);
    List<Order> getSortedOrders(OrderSortBy sortBy);
    boolean removeOrder(int orderId);
}
