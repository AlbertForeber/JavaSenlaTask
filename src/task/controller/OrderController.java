package task.controller;

import task.model.entity.sortby.OrderSortBy;
import task.model.entity.status.OrderStatus;
import task.service.facade.BookOrderFacade;

import java.util.List;

public class OrderController {
    BookOrderFacade bookOrderFacade;

    public OrderController(
            BookOrderFacade bookOrderFacade
    ) {
        this.bookOrderFacade = bookOrderFacade;
    }

    public void createOrder(int orderId, List<String> bookNames, String customerName) {
        bookOrderFacade.createOrder(orderId, bookNames, customerName);
    }

    public void cancelOrder(int orderId) {
        bookOrderFacade.cancelOrder(orderId);
    }

    public void changeOrderStatus(int orderId, OrderStatus newStatus) {
        bookOrderFacade.changeOrderStatus(orderId, newStatus);
    }

    public void getSorted(OrderSortBy sortBy) {
        bookOrderFacade.getSorted(sortBy);
    }

    public void getCompletedOrdersInInterval(
            int fromYear,
            int fromMonth,
            int fromDate,
            int toYear,
            int toMonth,
            int toDate
    ) {
        bookOrderFacade.getCompletedOrdersInInterval(fromYear, fromMonth, fromDate, toYear, toMonth, toDate);
    }

    public void getIncomeInInterval (
            int fromYear,
            int fromMonth,
            int fromDate,
            int toYear,
            int toMonth,
            int toDate
    ) {
        bookOrderFacade.getIncomeInInterval(fromYear, fromMonth, fromDate, toYear, toMonth, toDate);
    }

    public void getOrderAmountInInterval (
            int fromYear,
            int fromMonth,
            int fromDate,
            int toYear,
            int toMonth,
            int toDate
    ) {
        bookOrderFacade.getOrderAmountInInterval(fromYear, fromMonth, fromDate, toYear, toMonth, toDate);
    }

    public void getOrderDetails(int orderId) {
        bookOrderFacade.getOrderDetails(orderId);
    }
}