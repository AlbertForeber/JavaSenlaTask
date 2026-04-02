package com.senla.app.service.order;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.exceptions.ResourceNotFound;
import com.senla.app.exceptions.WrongId;
import com.senla.app.repository.OrderManagerRepository;
import com.senla.app.model.entity.Order;
import com.senla.app.model.entity.sortby.OrderSortBy;
import com.senla.app.model.entity.status.OrderStatus;
import com.senla.app.service.unit_of_work.UnitOfWork;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


// Сервис, а не фасад, так как содержит бизнес-логику, а не простые вызовы готовых методов
// + все методы отвечают за одну предметную область.

@Service
public class OrderQueryService {

    private final OrderManagerRepository orderManagerRepository;

    private final UnitOfWork unitOfWork;

    public OrderQueryService(
            @Db OrderManagerRepository orderManagerRepository,
            @Hibernate UnitOfWork unitOfWork
    ) {
        this.orderManagerRepository = orderManagerRepository;
        this.unitOfWork = unitOfWork;
    }

    @Transactional
    public List<Order> getSorted(OrderSortBy sortBy) {

        List<Order> result = orderManagerRepository.getSortedOrders(sortBy, true);
        if (result.isEmpty()) throw new ResourceNotFound("заказов нет");

        return result;
    }

    @Transactional
    public List<Order> getCompletedOrdersInInterval(
            int fromYear,
            int fromMonth,
            int fromDate,
            int toYear,
            int toMonth,
            int toDate,

            boolean getBooks
    ) {

        List<Order> orders = orderManagerRepository.getSortedOrders(OrderSortBy.PRICE_DATE, getBooks);
        if (orders.isEmpty()) throw new ResourceNotFound("заказов нет");

        List<Order> toReturn = new ArrayList<>();

        long from = new GregorianCalendar(fromYear, fromMonth - 1, fromDate).getTimeInMillis();
        long to = new GregorianCalendar(toYear, toMonth - 1, toDate).getTimeInMillis();

        for (Order order : orders) {
            if (order.getCompletionDate() == null) continue;

            long orderTime = order.getCompletionDate().getTimeInMillis();
            if (from <= orderTime && orderTime <= to && order.getStatus() == OrderStatus.COMPLETED) {
                toReturn.add(order);
            }
        }

        return toReturn;
    }

    @Transactional
    public long getIncomeInInterval(
            int fromYear,
            int fromMonth,
            int fromDate,
            int toYear,
            int toMonth,
            int toDate
    ) {

        int toReturn = 0;

        for (Order order : getCompletedOrdersInInterval(fromYear, fromMonth, fromDate, toYear, toMonth, toDate, false)) {
            toReturn += order.getTotalSum();
        }

        return toReturn;
    }

    @Transactional
    public int getOrderAmountInInterval(
            int fromYear,
            int fromMonth,
            int fromDate,
            int toYear,
            int toMonth,
            int toDate
    ) {
        return getCompletedOrdersInInterval(fromYear, fromMonth, fromDate, toYear, toMonth, toDate, false).size();
    }

    @Transactional
    public Order getOrderDetails(int orderId) {
        Order order = orderManagerRepository.getOrder(orderId, true);
        if (order == null) throw new WrongId("заказа #" + orderId + "не существует");

        return order;
    }
}
