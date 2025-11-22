package task.service.order;

import task.repository.OrderManagerRepository;
import task.model.entity.Order;
import task.model.entity.sortby.OrderSortBy;
import task.model.entity.status.OrderStatus;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


// Сервис, а не фасад, так как содержит бизнес-логику, а не простые вызовы готовых методов
// + все методы отвечают за одну предметную область.

public class OrderQueryService {

    private final OrderManagerRepository orderManagerRepository;

    public OrderQueryService(
            OrderManagerRepository orderManagerRepository
    ) {
        this.orderManagerRepository = orderManagerRepository;
    }

    public List<Order> getSorted(OrderSortBy sortBy) {
        return orderManagerRepository.getSortedOrders(sortBy);
    }

    public List<Order> getCompletedOrdersInInterval(
            int fromYear,
            int fromMonth,
            int fromDate,
            int toYear,
            int toMonth,
            int toDate
    ) {
        List<Order> orders = orderManagerRepository.getSortedOrders(OrderSortBy.PRICE_DATE);
        List<Order> toReturn = new ArrayList<>();

        long from = new GregorianCalendar(fromYear, fromMonth - 1, fromDate).getTimeInMillis();
        long to = new GregorianCalendar(toYear, toMonth - 1, toDate).getTimeInMillis();

        for (Order order : orders) {
            long orderTime = order.getCompletionDate().getTimeInMillis();
            if (from <= orderTime && orderTime <= to && order.getStatus() == OrderStatus.COMPLETED) {
                toReturn.add(order);
            }
        }

        return toReturn;
    }

    public long getIncomeInInterval (
            int fromYear,
            int fromMonth,
            int fromDate,
            int toYear,
            int toMonth,
            int toDate
    ) {
        int toReturn = 0;

        for (Order order : getCompletedOrdersInInterval(fromYear, fromMonth, fromDate, toYear, toMonth, toDate)) {
            toReturn += order.getTotalSum();
        }

        return toReturn;
    }

    public int getOrderAmountInInterval (
            int fromYear,
            int fromMonth,
            int fromDate,
            int toYear,
            int toMonth,
            int toDate
    ) {
        return getCompletedOrdersInInterval(fromYear, fromMonth, fromDate, toYear, toMonth, toDate).size();
    }

    public String getOrderDetails(int orderId) {
        return orderManagerRepository.getOrder(orderId).toString();
    }
}
