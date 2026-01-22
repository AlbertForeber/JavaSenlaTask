package com.senla.app.task.model.comparators.order;

import com.senla.app.task.model.entity.Order;

import java.util.Comparator;

public class OrderStatusComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
        return o1.getStatus().compareTo(o2.getStatus());
    }
}
