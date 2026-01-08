package com.senla.app.task.repository.db;

import com.senla.annotation.InjectTo;
import com.senla.app.task.db.dao.implementations.BookDao;
import com.senla.app.task.db.dao.implementations.OrderDao;
import com.senla.app.task.model.comparators.order.OrderComplDateComparator;
import com.senla.app.task.model.comparators.order.OrderComplDatePriceComparator;
import com.senla.app.task.model.comparators.order.OrderPriceComparator;
import com.senla.app.task.model.comparators.order.OrderStatusComparator;
import com.senla.app.task.model.dto.BookDto;
import com.senla.app.task.model.dto.OrderDto;
import com.senla.app.task.model.entity.Order;
import com.senla.app.task.model.entity.sortby.OrderSortBy;
import com.senla.app.task.repository.OrderManagerRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
            List<Integer> orderedBookIds = bookDao.findAll()
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
    public List<Order> getSortedOrders(OrderSortBy sortBy) {
        Comparator<Order> comparator = switch (sortBy) {
            case PRICE -> new OrderPriceComparator();
            case STATUS -> new OrderStatusComparator();
            case COMPLETION_DATE -> new OrderComplDateComparator();
            case PRICE_DATE -> new OrderComplDatePriceComparator();
            case NO_SORT -> null;
        };

        try {
            List<Order> arr = convertOrderDtosToOrders(orderDao.findAll());

            if (sortBy == OrderSortBy.COMPLETION_DATE || sortBy == OrderSortBy.PRICE_DATE) {
                arr = arr.stream().filter(x -> x.getCompletionDate() != null).collect(Collectors.toList());
            }

            if (comparator != null) arr.sort(comparator);
            return arr;
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

    private List<Order> convertOrderDtosToOrders(List<OrderDto> orderDtos) throws SQLException {
        List<BookDto> currentBooks = bookDao.findAll();
        HashMap<Integer, ArrayList<Integer>> orderToBooks = new HashMap<>();

        ArrayList<Order> toReturn = new ArrayList<>();

        for (OrderDto orderDto : orderDtos) {
            ArrayList<Integer> bookIds = new ArrayList<>();
            orderToBooks.put(orderDto.getId(), bookIds);
            toReturn.add(orderDto.toBusinessObject(bookIds));
        }

        for (BookDto i : currentBooks) {
            if (orderToBooks.containsKey(i.getOrderId())) {
                orderToBooks.get(i.getOrderId()).add(i.getId());
            }
        }

        return toReturn;
    }
}
