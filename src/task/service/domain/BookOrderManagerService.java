package task.service.domain;

import task.model.comparators.order.OrderComplDateComparator;
import task.model.comparators.order.OrderComplDatePriceComparator;
import task.model.comparators.order.OrderPriceComparator;
import task.model.comparators.order.OrderStatusComparator;
import task.model.entity.Order;
import task.model.entity.sortby.OrderSortBy;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class BookOrderManager implements OrderManager {
    private final HashMap<Integer, Order> orders = new HashMap<>();


    @Override
    public void addOrder(int orderId, Order order) {
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

    public HashMap<Integer, Order> getOrderMap() {
        return orders;
    }

    @Override
    public Order[] getSortedOrders(OrderSortBy sortBy) {
        Comparator<Order> comparator = switch (sortBy) {
            case PRICE -> new OrderPriceComparator();
            case STATUS -> new OrderStatusComparator();
            case COMPLETION_DATE -> new OrderComplDateComparator();
            case PRICE_DATE -> new OrderComplDatePriceComparator();
            case NO_SORT -> null;
        };

        Order[] arr = orders.values().toArray(new Order[0]);
        if (comparator != null) Arrays.sort(arr, comparator);

        return arr;
    }
}
