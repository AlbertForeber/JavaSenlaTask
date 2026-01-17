package com.senla.app.task.repository.db;

import com.senla.annotation.InjectTo;
import com.senla.app.task.db.dao.jdbc_implementations.BookDao;
import com.senla.app.task.db.dao.jdbc_implementations.OrderDao;
import com.senla.app.task.model.dto.jdbc.BookDto;
import com.senla.app.task.model.dto.jdbc.OrderDto;
import com.senla.app.task.model.entity.Order;
import com.senla.app.task.model.entity.sortby.OrderSortBy;
import com.senla.app.task.repository.OrderManagerRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DbOrderManagerRepository implements OrderManagerRepository {

    @InjectTo
    OrderDao orderDao;

    @InjectTo
    BookDao bookDao;

    @Override
    public void addOrder(int orderId, Order order) throws IllegalArgumentException {
        try {
            orderDao.save(new OrderDto(order));
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public void updateOrder(Order order) {
        try {
            orderDao.update(new OrderDto(order));
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public Order getOrder(int orderId) {
        try {
            List<Integer> orderedBookIds = bookDao.findAll(null)
                    .stream()
                    .filter(o -> o.getOrderId() == orderId)
                    .map(BookDto::getId).toList();

            OrderDto orderDto = orderDao.findById(orderId);

            if (orderDto == null) return null;
            return orderDto.toBusinessObject(orderedBookIds);
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public List<Order> getSortedOrders(OrderSortBy sortBy, boolean getBooks) {
        List<String> sortByList = new ArrayList<>();

        switch (sortBy) {
            case PRICE -> sortByList.add("total_sum");
            case STATUS -> sortByList.add("status");
            case COMPLETION_DATE -> sortByList.add("completion_date");
            case PRICE_DATE -> sortByList.addAll(List.of("total_sum", "completion_date"));
        }

        try {
            return convertOrderDtosToOrders(
                    orderDao.findAll(sortBy != OrderSortBy.NO_SORT ? sortByList : null),
                    getBooks
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    @Override
    public boolean removeOrder(int orderId) {
        try {
            orderDao.delete(orderId);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("Исключение БД: " + e);
        }
    }

    private List<Order> convertOrderDtosToOrders(List<OrderDto> orderDtos, boolean getBooks) throws SQLException {

        HashMap<Integer, ArrayList<Integer>> orderToBooks = new HashMap<>();

        ArrayList<Order> toReturn = new ArrayList<>();

        for (OrderDto orderDto : orderDtos) {
            ArrayList<Integer> bookIds = getBooks ? new ArrayList<>() : null;
            orderToBooks.put(orderDto.getId(), bookIds);
            toReturn.add(orderDto.toBusinessObject(bookIds));
        }

        if (getBooks) {
            List<BookDto> currentBooks = bookDao.findAll(null);
            for (BookDto i : currentBooks) {
                if (orderToBooks.containsKey(i.getOrderId())) {
                    orderToBooks.get(i.getOrderId()).add(i.getId());
                }
            }
        }

        return toReturn;
    }
}
