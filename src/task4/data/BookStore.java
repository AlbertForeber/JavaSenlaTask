package task4.data;

import task4.data.dto.*;
import task4.domain.RequestManager;
import task4.domain.Storage;

import java.util.List;

public class BookStore {
    // public for debugging
    public BookOrderManager orderManager = new BookOrderManager();
    public Storage bookStorage = new BookStorage();
    public RequestManager requestManager = new BookRequestManager();

    public void writeOffBook(String bookName) {
        bookStorage.getBook(bookName).setStatus(BookStatus.SOLD_OUT);
    }

    public void createOrder(int orderId, List<String> bookNames, String customerName) {
        Order order = new Order(orderId, bookNames, customerName);
        orderManager.addOrder(orderId, order);

        for (String bookName : bookNames) {
            bookStorage.getBook(bookName).setStatus(BookStatus.RESERVED);
        }
    }

    public void cancelOrder(int orderId) {
        Order order = orderManager.getOrder(orderId);
        order.setStatus(OrderStatus.CANCELED);

        for (String bookName : order.getOrderedBookNames()) {
            bookStorage.getBook(bookName).setStatus(BookStatus.FREE);
        }
    }

    public void changeOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = orderManager.getOrder(orderId);
        order.setStatus(newStatus);

        BookStatus requiredBookStatus = BookStatus.RESERVED;

        switch (newStatus) {
            case CANCELED -> requiredBookStatus = BookStatus.FREE;
            case COMPLETED -> requiredBookStatus = BookStatus.SOLD_OUT;
        }

        for (String bookName : order.getOrderedBookNames()) {
            bookStorage.getBook(bookName).setStatus(requiredBookStatus);
        }
    }

    public void addBookToStorage(String bookName) {
        bookStorage.getBook(bookName).setStatus(BookStatus.FREE);
        requestManager.cancelRequests(bookName);
    }

    public void createRequest(String bookName) {
        Request request = new Request(bookName);
        requestManager.addRequest(request);
    }


}
