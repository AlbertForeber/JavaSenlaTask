package task.repository;

import task.model.entity.Order;
import task.model.entity.sortby.OrderSortBy;

import java.util.List;

public interface OrderManagerRepository {
    void addOrder(int orderId, Order order);
    Order getOrder(int orderId);
    List<Order> getSortedOrders(OrderSortBy sortBy);
    boolean removeOrder(int orderId);
}
