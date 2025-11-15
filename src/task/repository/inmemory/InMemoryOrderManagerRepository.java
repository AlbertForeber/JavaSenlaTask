package task.repository.inmemory;

import task.model.entity.status.OrderStatus;
import task.repository.OrderManagerRepository;
import task.model.comparators.order.OrderComplDateComparator;
import task.model.comparators.order.OrderComplDatePriceComparator;
import task.model.comparators.order.OrderPriceComparator;
import task.model.comparators.order.OrderStatusComparator;
import task.model.entity.Order;
import task.model.entity.sortby.OrderSortBy;
import task.utils.DataConverter;

import java.io.*;
import java.security.InvalidParameterException;
import java.security.KeyException;
import java.util.*;

public class InMemoryOrderManagerRepository implements OrderManagerRepository {
    private final HashMap<Integer, Order> orders = new HashMap<>();


    @Override
    public void addOrder(int orderId, Order order) throws IllegalArgumentException {
        if (orders.containsKey(orderId)) {
            throw new IllegalArgumentException("Заказ уже существует");
        }
        orders.put(orderId, order);
    }

    @Override
    public Order getOrder(int orderId) {
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
    public List<Order> getSortedOrders(OrderSortBy sortBy) {
        Comparator<Order> comparator = switch (sortBy) {
            case PRICE -> new OrderPriceComparator();
            case STATUS -> new OrderStatusComparator();
            case COMPLETION_DATE -> new OrderComplDateComparator();
            case PRICE_DATE -> new OrderComplDatePriceComparator();
            case NO_SORT -> null;
        };

        List<Order> arr = new ArrayList<>(orders.values().stream().toList());

        if (comparator != null) arr.sort(comparator);
        return arr;
    }
}


