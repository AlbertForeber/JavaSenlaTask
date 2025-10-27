package task4.data;

import task4.data.dto.Book;
import task4.data.dto.Order;
import task4.domain.OrderManager;
import task4.data.dto.OrderStatus;

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
}
