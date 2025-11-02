package task.domain;

import task.data.dto.Order;
import task.data.dto.sortby.OrderSortBy;

public interface OrderManager {
    void addOrder(int orderId, Order order);
    Order getOrder(int orderId);
    Order[] getSortedOrdersBy(OrderSortBy sortBy);
    boolean removeOrder(int orderId);
}
