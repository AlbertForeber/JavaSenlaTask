package task.service.order;

import task.repository.OrderManagerRepository;
import task.repository.RequestManagerRepository;
import task.repository.StorageRepository;
import task.model.entity.Book;
import task.model.entity.Order;
import task.model.entity.Request;
import task.model.entity.status.BookStatus;
import task.model.entity.status.OrderStatus;
import task.utils.DataConverter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


// Сервис, а не фасад, так как содержит бизнес-логику, а не простые вызовы готовых методов
// + все методы отвечают за одну предметную область.

public class OrderService {

    private final OrderManagerRepository orderManagerRepository;
    private final StorageRepository bookStorageRepository;
    private final RequestManagerRepository requestManagerRepository;

    public OrderService(
            OrderManagerRepository orderManagerRepository,
            StorageRepository storageRepository,
            RequestManagerRepository requestManagerRepository
    ) {
        this.bookStorageRepository = storageRepository;
        this.orderManagerRepository = orderManagerRepository;
        this.requestManagerRepository = requestManagerRepository;
    }

    public boolean createOrder(int orderId, List<String> bookNames, String customerName) {
        int totalSum = 0;
        List<String> copyOfBookNames = new ArrayList<>(bookNames);

        for (String bookName : bookNames) {
            Book book = bookStorageRepository.getBook(bookName);

            // Проверка наличия книги
            if (book == null) {
                copyOfBookNames.remove(bookName);
                continue;
            }

            else if (book.getStatus() != BookStatus.FREE) {
                Request request = new Request(bookName);
                requestManagerRepository.addRequest(request);

            } else book.setStatus(BookStatus.RESERVED, customerName);

            totalSum += book.getPrice();
        }

        if (!copyOfBookNames.isEmpty()) {
            Order order = new Order(orderId, bookNames, totalSum, customerName);
            orderManagerRepository.addOrder(orderId, order);
            return true;
        } else return false;

    }

    public void cancelOrder(int orderId) {
        Order order = orderManagerRepository.getOrder(orderId);
        order.setStatus(OrderStatus.CANCELED);

        for (String bookName : order.getOrderedBookNames()) {
            bookStorageRepository.getBook(bookName).setStatus(BookStatus.FREE);
        }
    }

    public boolean changeOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = orderManagerRepository.getOrder(orderId);

        if (newStatus == OrderStatus.COMPLETED) {
            for (String bookName:  order.getOrderedBookNames()) {
                Book book = bookStorageRepository.getBook(bookName);
                if (book.getStatus() != BookStatus.FREE && !Objects.equals(book.getReservist(), order.getCustomerName())) {
                    return false;
                }
            }

            // Обновление даты
            Calendar currentDate = Calendar.getInstance();
            order.setCompletionDate(
                    currentDate.get(Calendar.YEAR),
                    currentDate.get(Calendar.MONTH) + 1,
                    currentDate.get(Calendar.DATE)
            );
        }
        order.setStatus(newStatus);

        BookStatus requiredBookStatus = BookStatus.RESERVED;

        switch (newStatus) {
            case CANCELED -> requiredBookStatus = BookStatus.FREE;
            case COMPLETED -> requiredBookStatus = BookStatus.SOLD_OUT;
        }

        for (String bookName : order.getOrderedBookNames()) {
            bookStorageRepository.getBook(bookName).setStatus(requiredBookStatus);
        }

        return true;
    }
}
