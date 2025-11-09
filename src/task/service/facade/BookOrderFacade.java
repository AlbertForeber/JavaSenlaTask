package task.service.facade;

import task.model.entity.Book;
import task.model.entity.Order;
import task.model.entity.Request;
import task.model.entity.sortby.OrderSortBy;
import task.model.entity.status.BookStatus;
import task.model.entity.status.OrderStatus;
import task.service.domain.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

public class BookOrderFacade {

    private final OrderManagerService orderManagerService;
    private final StorageService bookStorageService;
    private final RequestManagerService requestManagerService;

    public BookOrderFacade(
            OrderManagerService orderManagerService,
            StorageService storageService,
            RequestManagerService requestManagerService
    ) {
        this.bookStorageService = storageService;
        this.orderManagerService = orderManagerService;
        this.requestManagerService = requestManagerService;
    }

    public void createOrder(int orderId, List<String> bookNames, String customerName) {
        int totalSum = 0;

        for (String bookName : bookNames) {

            if (bookStorageService.getBook(bookName).getStatus() != BookStatus.FREE) {
                Request request = new Request(bookName);
                requestManagerService.addRequest(request);

            } else bookStorageService.getBook(bookName).setStatus(BookStatus.RESERVED, customerName);

            totalSum += bookStorageService.getBook(bookName).getPrice();
        }

        Order order = new Order(orderId, bookNames, totalSum, customerName);
        orderManagerService.addOrder(orderId, order);
    }

    public void cancelOrder(int orderId) {
        Order order = orderManagerService.getOrder(orderId);
        order.setStatus(OrderStatus.CANCELED);

        for (String bookName : order.getOrderedBookNames()) {
            bookStorageService.getBook(bookName).setStatus(BookStatus.FREE);
        }
    }

    public void changeOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = orderManagerService.getOrder(orderId);

        if (newStatus == OrderStatus.COMPLETED) {
            for (String bookName:  order.getOrderedBookNames()) {
                Book book = bookStorageService.getBook(bookName);
                if (book.getStatus() != BookStatus.FREE && !Objects.equals(book.getReservist(), order.getCustomerName())) {
                    return;
                }
            }
        }
        order.setStatus(newStatus);

        BookStatus requiredBookStatus = BookStatus.RESERVED;

        switch (newStatus) {
            case CANCELED -> requiredBookStatus = BookStatus.FREE;
            case COMPLETED -> requiredBookStatus = BookStatus.SOLD_OUT;
        }

        for (String bookName : order.getOrderedBookNames()) {
            bookStorageService.getBook(bookName).setStatus(requiredBookStatus);
        }
    }

    public Order[] getSorted(OrderSortBy sortBy) {
        return orderManagerService.getSortedOrders(sortBy);
    }

    public Order[] getCompletedOrdersInInterval(
            int fromYear,
            int fromMonth,
            int fromDate,
            int toYear,
            int toMonth,
            int toDate
    ) {
        Order[] orders = orderManagerService.getSortedOrders(OrderSortBy.PRICE_DATE);
        ArrayList<Order> toReturn = new ArrayList<>();

        long from = new GregorianCalendar(fromYear, fromMonth - 1, fromDate).getTimeInMillis();
        long to = new GregorianCalendar(toYear, toMonth - 1, toDate).getTimeInMillis();

        for (Order order : orders) {
            long orderTime = order.getCompletionDate().getTimeInMillis();
            if (from <= orderTime && orderTime <= to && order.getStatus() == OrderStatus.COMPLETED) {
                toReturn.add(order);
            }
        }

        return toReturn.toArray(new Order[0]);
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
        return getCompletedOrdersInInterval(fromYear, fromMonth, fromDate, toYear, toMonth, toDate).length;
    }

    public String getOrderDetails(int orderId) {
        return orderManagerService.getOrder(orderId).toString();
    }
}
