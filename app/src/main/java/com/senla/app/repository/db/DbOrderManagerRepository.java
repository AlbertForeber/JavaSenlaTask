package com.senla.app.repository.db;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.db.dao.GenericDao;
import com.senla.app.exceptions.DataManipulationException;
import com.senla.app.model.entity.Order;
import com.senla.app.model.entity.sortby.OrderSortBy;
import com.senla.app.repository.OrderManagerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Db
public class DbOrderManagerRepository implements OrderManagerRepository {

    private final GenericDao<Order, Integer, OrderSortBy> orderDao;

    public DbOrderManagerRepository(@Hibernate GenericDao<Order, Integer, OrderSortBy> orderDao) throws DataManipulationException {
        this.orderDao = orderDao;
    }

    @Override
    public void addOrder(int orderId, Order order) throws DataManipulationException {
        orderDao.save(order);
    }

    @Override
    public void updateOrder(Order order) throws DataManipulationException {
        orderDao.update(order);
    }

    @Override
    public Order getOrder(int orderId, boolean getLinkedObjects) throws DataManipulationException {
        return orderDao.findById(orderId, getLinkedObjects);
    }

    @Override
    public List<Order> getSortedOrders(OrderSortBy sortBy, boolean getLinkedObjects) throws DataManipulationException {
        return orderDao.findAll(sortBy != OrderSortBy.NO_SORT ? sortBy : null, getLinkedObjects);
    }

    @Override
    public boolean removeOrder(int orderId) throws DataManipulationException {
        orderDao.delete(orderId);
        return true;
    }
}
