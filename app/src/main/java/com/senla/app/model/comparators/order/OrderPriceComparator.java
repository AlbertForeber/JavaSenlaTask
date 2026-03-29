package com.senla.app.model.comparators.order;

import com.senla.app.model.entity.Order;

import java.util.Comparator;

public class OrderPriceComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
        return Integer.compare(o1.getTotalSum(), o2.getTotalSum());
    }
}
