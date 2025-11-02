package task.data;

import task.data.dto.*;
import task.data.dto.sortby.BookSortBy;
import task.data.dto.sortby.OrderSortBy;
import task.data.dto.sortby.RequestSortBy;
import task.data.dto.status.BookStatus;
import task.data.dto.status.OrderStatus;
import task.domain.RequestManager;
import task.domain.Storage;

import java.util.*;

public class BookStore {
    // public for debugging
    public BookOrderManager orderManager = new BookOrderManager();
    public Storage bookStorage = new BookStorage();
    public RequestManager requestManager = new BookRequestManager();

    public void writeOffBook(String bookName) {
        bookStorage.getBook(bookName).setStatus(BookStatus.SOLD_OUT, null);
    }

    public void createOrder(int orderId, List<String> bookNames, String customerName) {
        int totalSum = 0;

        for (String bookName : bookNames) {

            if (bookStorage.getBook(bookName).getStatus() != BookStatus.FREE) {
                Request request = new Request(bookName);
                requestManager.addRequest(request);

            } else bookStorage.getBook(bookName).setStatus(BookStatus.RESERVED, customerName);

            totalSum += bookStorage.getBook(bookName).getPrice();
        }

        Order order = new Order(orderId, bookNames, totalSum, customerName);
        orderManager.addOrder(orderId, order);
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

        if (newStatus == OrderStatus.COMPLETED) {
            for (String bookName:  order.getOrderedBookNames()) {
                Book book = bookStorage.getBook(bookName);
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

    public Book[] getSorted(BookSortBy sortBy) {
        return bookStorage.getSortedBooks(sortBy);
    }

    public Order[] getSorted(OrderSortBy sortBy) {
        return orderManager.getSortedOrders(sortBy);
    }

    public Request[] getSorted(RequestSortBy sortBy) {
        return requestManager.getSortedRequests(sortBy);
    }

    public Order[] getCompletedOrdersInInterval(
            int fromYear,
            int fromMonth,
            int fromDate,
            int toYear,
            int toMonth,
            int toDate
    ) {
        Order[] orders = orderManager.getSortedOrders(OrderSortBy.PRICE_DATE);
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

    public Book[] getHardToSell(
            int nowYear,
            int nowMonth,
            int nowDate
    ) {
        Book[] books = bookStorage.getSortedBooks(BookSortBy.DATE_PRICE);
        ArrayList<Book> toReturn = new ArrayList<>();

        Calendar now = new GregorianCalendar(nowYear, nowMonth, nowDate);

        for (Book book : books) {
            if (now.get(Calendar.MONTH) - book.getAdmissionDate().get(Calendar.MONTH) >= 6
                    && book.getStatus() == BookStatus.FREE) {
                toReturn.add(book);
            }
        }

        return toReturn.toArray(new Book[0]);
    }

    public String getOrderDetails(int orderId) {
        return orderManager.getOrder(orderId).toString();
    }

    public String getBookDescription(String bookName) {
        return bookStorage.getBook(bookName).getDescription();
    }
}
