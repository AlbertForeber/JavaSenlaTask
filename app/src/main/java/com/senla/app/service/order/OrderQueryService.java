package com.senla.app.service.order;

import com.senla.annotation.InjectTo;
import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.repository.OrderManagerRepository;
import com.senla.app.model.entity.Order;
import com.senla.app.model.entity.sortby.OrderSortBy;
import com.senla.app.model.entity.status.OrderStatus;
import com.senla.app.repository.db.DbOrderManagerRepository;
import com.senla.app.service.unit_of_work.UnitOfWork;
import com.senla.app.service.unit_of_work.implementations.HibernateUnitOfWork;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


// Сервис, а не фасад, так как содержит бизнес-логику, а не простые вызовы готовых методов
// + все методы отвечают за одну предметную область.

@Service
public class OrderQueryService {

    @InjectTo(useImplementation = DbOrderManagerRepository.class)
    private final OrderManagerRepository orderManagerRepository;

    @InjectTo(useImplementation = HibernateUnitOfWork.class)
    private final UnitOfWork unitOfWork;

    public OrderQueryService(
            @Db OrderManagerRepository orderManagerRepository,
            @Hibernate UnitOfWork unitOfWork
    ) {
        this.orderManagerRepository = orderManagerRepository;
        this.unitOfWork = unitOfWork;
    }

    public List<Order> getSorted(OrderSortBy sortBy) {
        return unitOfWork.execute(() -> orderManagerRepository.getSortedOrders(sortBy, true));
    }

    public List<Order> getCompletedOrdersInInterval(
            int fromYear,
            int fromMonth,
            int fromDate,
            int toYear,
            int toMonth,
            int toDate,

            boolean getBooks
    ) {
        return unitOfWork.execute(() -> {
                List<Order> orders = orderManagerRepository.getSortedOrders(OrderSortBy.PRICE_DATE, getBooks);
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
        );
    }

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

    public String getOrderDetails(int orderId) {
        return unitOfWork.execute(() -> {
            Order order = orderManagerRepository.getOrder(orderId, true);
            return order == null ? null : order.toString();
        });
    }

    public void saveState(String path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path + "order"))) {
            for (Order order : orderManagerRepository.getSortedOrders(OrderSortBy.NO_SORT, true)) {
                oos.writeObject(order);
            }
        }
    }

    public void loadState(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path + "order"))) {
            Order order;
            while (true) {
                try {
                    order = (Order) ois.readObject();
                    orderManagerRepository.addOrder(order.getId(), order);
                } catch (EOFException e) {
                    break;
                }
            }
        }
    }
}
