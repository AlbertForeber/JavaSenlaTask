package com.senla.app.task.repository.inmemory;

import com.senla.app.task.repository.OrderManagerRepository;
import com.senla.app.task.model.comparators.order.OrderComplDateComparator;
import com.senla.app.task.model.comparators.order.OrderComplDatePriceComparator;
import com.senla.app.task.model.comparators.order.OrderPriceComparator;
import com.senla.app.task.model.comparators.order.OrderStatusComparator;
import com.senla.app.task.model.entity.Order;
import com.senla.app.task.model.entity.sortby.OrderSortBy;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryOrderManagerRepository implements OrderManagerRepository {

    private final HashMap<Integer, Order> orders = new HashMap<>();


    @Override
    public void addOrder(int orderId, Order order) throws IllegalArgumentException {
        orders.put(orderId, order);
    }

    @Override
    public void updateOrder(Order order) { } // InMemory вариант не должен выполнять доп. операцию обновления

    @Override
    public Order getOrder(int orderId, boolean getLinkedObjects) {
        if (orders.containsKey(orderId)) {
            return orders.get(orderId);
        }
        return null;
    }

    @Override
    public boolean removeOrder(int orderId) {
        return orders.remove(orderId) != null;
    }

    @Override
    public List<Order> getSortedOrders(OrderSortBy sortBy, boolean getLinkedObjects) {
        Comparator<Order> comparator = switch (sortBy) {
            case PRICE -> new OrderPriceComparator();
            case STATUS -> new OrderStatusComparator();
            case COMPLETION_DATE -> new OrderComplDateComparator();
            case PRICE_DATE -> new OrderComplDatePriceComparator();
            case NO_SORT -> null;
        };

        List<Order> arr = new ArrayList<>(orders.values());

        if (sortBy == OrderSortBy.COMPLETION_DATE || sortBy == OrderSortBy.PRICE_DATE) {
            arr = arr.stream().filter(x -> x.getCompletionDate() != null).collect(Collectors.toList());
        }

        if (comparator != null) arr.sort(comparator);
        return arr;
    }
}


