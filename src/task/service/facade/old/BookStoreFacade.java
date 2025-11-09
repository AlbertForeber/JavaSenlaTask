package task.service.facade;

import task.model.entity.Book;
import task.model.entity.Order;
import task.model.entity.Request;
import task.model.entity.sortby.BookSortBy;
import task.model.entity.sortby.OrderSortBy;
import task.model.entity.sortby.RequestSortBy;
import task.model.entity.status.BookStatus;
import task.model.entity.status.OrderStatus;
import task.service.domain.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BookStoreFacade {
    // public for debugging
    public BookOrderManagerService orderManagerService = new BookOrderManagerService();
    public StorageService bookStorageService = new BookStorageService();
    public RequestManagerService requestManagerService = new BookRequestManagerService();

    public void writeOffBook(String bookName) {
        bookStorageService.getBook(bookName).setStatus(BookStatus.SOLD_OUT, null);
    }

    public void createOrder(int orderId, List<String> bookNames, String customerName) {
        AtomicInteger totalSum = new AtomicInteger();

        bookNames.stream()
                .peek(bookName -> totalSum.addAndGet(bookStorageService.getBook(bookName).getPrice()))
                .collect(Collections.list());
        
        for (String bookName : bookNames) {

            if (bookStorageService.getBook(bookName).getStatus() != BookStatus.FREE) {
                Request request = new Request(bookName);
                requestManagerService.addRequest(request);

            } else bookStorageService.getBook(bookName).setStatus(BookStatus.RESERVED, customerName);

            totalSum.addAndGet(bookStorageService.getBook(bookName).getPrice());
        }

        Order order = new Order(orderId, bookNames, totalSum.get(), customerName);
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

    public void addBookToStorage(String bookName) {
        bookStorageService.getBook(bookName).setStatus(BookStatus.FREE);
        requestManagerService.cancelRequests(bookName);
    }

    public void createRequest(String bookName) {
        Request request = new Request(bookName);
        requestManagerService.addRequest(request);
    }

    public Book[] getSorted(BookSortBy sortBy) {
        return bookStorageService.getSortedBooks(sortBy);
    }

    public Order[] getSorted(OrderSortBy sortBy) {
        return orderManagerService.getSortedOrders(sortBy);
    }

    public Request[] getSorted(RequestSortBy sortBy) {
        return requestManagerService.getSortedRequests(sortBy);
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

    public Book[] getHardToSell(
            int nowYear,
            int nowMonth,
            int nowDate
    ) {
        Book[] books = bookStorageService.getSortedBooks(BookSortBy.DATE_PRICE);
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
        return orderManagerService.getOrder(orderId).toString();
    }

    public String getBookDescription(String bookName) {
        return bookStorageService.getBook(bookName).getDescription();
    }
}
