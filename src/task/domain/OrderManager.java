package task4.domain;

import task4.data.dto.Order;
import task4.data.dto.OrderStatus;

public interface OrderManager {
    void addOrder(int orderId, Order order);
    Order getOrder(int orderId);
    boolean removeOrder(int orderId);
}
