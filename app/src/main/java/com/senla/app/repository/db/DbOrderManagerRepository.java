package com.senla.app.repository.db;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.db.DatabaseException;
import com.senla.app.db.dao.GenericDao;
import com.senla.app.model.entity.Order;
import com.senla.app.model.entity.sortby.OrderSortBy;
import com.senla.app.repository.OrderManagerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Db
public class DbOrderManagerRepository implements OrderManagerRepository {

    private final GenericDao<Order, Integer, OrderSortBy> orderDao;

    public DbOrderManagerRepository(@Hibernate GenericDao<Order, Integer, OrderSortBy> orderDao) {
        this.orderDao = orderDao;
    }

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
