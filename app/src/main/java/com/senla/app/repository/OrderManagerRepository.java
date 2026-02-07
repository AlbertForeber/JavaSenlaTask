package com.senla.app.repository;

import com.senla.app.model.entity.Order;
import com.senla.app.model.entity.sortby.OrderSortBy;

import java.util.List;

public interface OrderManagerRepository {

    void addOrder(int orderId, Order order);
    void updateOrder(Order order);
    Order getOrder(int orderId, boolean getLinkedObjects);
    List<Order> getSortedOrders(OrderSortBy sortBy, boolean getLinkedObjects);
    boolean removeOrder(int orderId);
}
