package task.service.domain;

import task.model.entity.Order;
import task.model.entity.sortby.OrderSortBy;

public interface OrderManager {
    void addOrder(int orderId, Order order);
    Order getOrder(int orderId);
    Order[] getSortedOrders(OrderSortBy sortBy);
    boolean removeOrder(int orderId);
}
