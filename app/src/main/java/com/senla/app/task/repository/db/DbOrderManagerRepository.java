package com.senla.app.task.repository.db;

import com.senla.annotation.InjectTo;
import com.senla.app.task.db.DatabaseException;
import com.senla.app.task.db.dao.GenericDao;
import com.senla.app.task.db.dao.hibernate_implementations.OrderDao;
import com.senla.app.task.model.entity.Order;
import com.senla.app.task.model.entity.sortby.OrderSortBy;
import com.senla.app.task.repository.OrderManagerRepository;

import java.util.List;

public class DbOrderManagerRepository implements OrderManagerRepository {

    @InjectTo(useImplementation = OrderDao.class)
    GenericDao<Order, Integer, OrderSortBy> orderDao;

    @Override
    public void addOrder(int orderId, Order order) throws IllegalArgumentException {
        try {
            orderDao.save(order);
        } catch (DatabaseException e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public void updateOrder(Order order) {
        try {
            orderDao.update(order);
        } catch (DatabaseException e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public Order getOrder(int orderId, boolean getLinkedObjects) {
        try {
            return orderDao.findById(orderId, getLinkedObjects);
        } catch (DatabaseException e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public List<Order> getSortedOrders(OrderSortBy sortBy, boolean getLinkedObjects) {

        try {
            return orderDao.findAll(sortBy != OrderSortBy.NO_SORT ? sortBy : null, getLinkedObjects);
        } catch (DatabaseException e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public boolean removeOrder(int orderId) {
        try {
            orderDao.delete(orderId);
            return true;
        } catch (DatabaseException e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }
}
