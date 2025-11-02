package task.data;

import task.data.comparators.book.*;
import task.data.comparators.order.OrderComplDateComparator;
import task.data.comparators.order.OrderComplDatePriceComparator;
import task.data.comparators.order.OrderPriceComparator;
import task.data.comparators.order.OrderStatusComparator;
import task.data.dto.Book;
import task.data.dto.Order;
import task.data.dto.sortby.OrderSortBy;
import task.domain.OrderManager;

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
    public Order[] getSortedOrdersBy(OrderSortBy sortBy) {
        Comparator<Order> comparator = switch (sortBy) {
            case PRICE -> new OrderPriceComparator();
            case STATUS -> new OrderStatusComparator();
            case COMPLETION_DATE -> new OrderComplDateComparator();
            case PRICE_DATE -> new OrderComplDatePriceComparator();
        };

        Order[] arr = orders.values().toArray(new Order[0]);
        Arrays.sort(arr, comparator);
        return arr;
    }
}
