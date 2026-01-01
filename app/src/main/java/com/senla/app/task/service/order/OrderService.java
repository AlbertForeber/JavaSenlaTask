package com.senla.app.task.service.order;

import com.senla.annotation.InjectTo;
import com.senla.app.task.model.dto.BookDto;
import com.senla.app.task.repository.OrderManagerRepository;
import com.senla.app.task.repository.RequestManagerRepository;
import com.senla.app.task.repository.StorageRepository;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.Order;
import com.senla.app.task.model.entity.status.BookStatus;
import com.senla.app.task.model.entity.status.OrderStatus;
import com.senla.app.task.repository.db.DbOrderManagerRepository;
import com.senla.app.task.repository.db.DbRequestManagerRepository;
import com.senla.app.task.repository.db.DbStorageRepository;

import java.util.*;


// Сервис, а не фасад, так как содержит бизнес-логику, а не простые вызовы готовых методов
// + все методы отвечают за одну предметную область.

public class OrderService {

    @InjectTo(useImplementation = DbOrderManagerRepository.class)
    private OrderManagerRepository orderManagerRepository;

    @InjectTo(useImplementation = DbStorageRepository.class)
    private StorageRepository bookStorageRepository;

    @InjectTo(useImplementation = DbRequestManagerRepository.class)
    private RequestManagerRepository requestManagerRepository;

    public OrderService() {}

    public boolean createOrder(int orderId, List<Integer> bookIds, String customerName) {
        int totalSum = 0;
        List<Book> presentBooks = new ArrayList<>();

        for (int id : bookIds) {
            Book book = bookStorageRepository.getBook(id);

            // Проверка наличия книги
            if (book != null) {
                presentBooks.add(book);
            } else continue;

            if (book.getStatus() != BookStatus.FREE) {;
                requestManagerRepository.addRequest(book);

            } else {
                book.setStatus(BookStatus.RESERVED, customerName);
            }

            totalSum += book.getPrice();
        }

        if (!presentBooks.isEmpty()) {
            Order order = new Order(orderId, presentBooks.stream().map(Book::getId).toList(), totalSum, customerName);

            if (orderManagerRepository.getOrder(orderId) != null) {
                cancelOrder(orderId);
            }

            orderManagerRepository.addOrder(orderId, order);

            for (Book book : presentBooks) {
                bookStorageRepository.updateBook(new BookDto(book, orderId));
            }
            return true;
        } else return false;

    }

    public void cancelOrder(int orderId) {
        Order order = orderManagerRepository.getOrder(orderId);
        order.setStatus(OrderStatus.CANCELED);

        orderManagerRepository.updateOrder(order);

        for (int bookId : order.getOrderedBookIds()) {
            Book book = bookStorageRepository.getBook(bookId);
            book.setStatus(BookStatus.FREE);

            bookStorageRepository.updateBook(new BookDto(book, null));
        }
    }

    public boolean changeOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = orderManagerRepository.getOrder(orderId);

        if (newStatus == OrderStatus.COMPLETED) {
            for (int bookId : order.getOrderedBookIds()) {
                Book book = bookStorageRepository.getBook(bookId);
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
        orderManagerRepository.updateOrder(order);

        BookStatus requiredBookStatus = BookStatus.RESERVED;

        switch (newStatus) {
            case CANCELED -> requiredBookStatus = BookStatus.FREE;
            case COMPLETED -> requiredBookStatus = BookStatus.SOLD_OUT;
        }

        for (int bookId : order.getOrderedBookIds()) {

            Book book = bookStorageRepository.getBook(bookId);
            book.setStatus(requiredBookStatus);

            bookStorageRepository
                    .updateBook(new BookDto(book, requiredBookStatus == BookStatus.RESERVED ? orderId : null));
        }

        return true;
    }
}
