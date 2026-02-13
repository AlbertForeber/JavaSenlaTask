package com.senla.app.service.order;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.exceptions.UnavailableAction;
import com.senla.app.exceptions.WrongId;
import com.senla.app.repository.OrderManagerRepository;
import com.senla.app.repository.RequestManagerRepository;
import com.senla.app.repository.StorageRepository;
import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.Order;
import com.senla.app.model.entity.status.BookStatus;
import com.senla.app.model.entity.status.OrderStatus;
import com.senla.app.service.unit_of_work.UnitOfWork;
import com.senla.app.service.unit_of_work.implementations.HibernateUnitOfWork;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


// Сервис, а не фасад, так как содержит бизнес-логику, а не простые вызовы готовых методов
// + все методы отвечают за одну предметную область.

@Service
public class OrderService {

    private final OrderManagerRepository orderManagerRepository;

    private final StorageRepository bookStorageRepository;

    private final RequestManagerRepository requestManagerRepository;

    private final UnitOfWork unitOfWork;

    public OrderService(
            @Db OrderManagerRepository orderManagerRepository,
            @Db StorageRepository bookStorageRepository,
            @Db RequestManagerRepository requestManagerRepository,
            @Hibernate UnitOfWork unitOfWork) {
        this.orderManagerRepository = orderManagerRepository;
        this.bookStorageRepository = bookStorageRepository;
        this.requestManagerRepository = requestManagerRepository;
        this.unitOfWork = unitOfWork;
    }

    @Transactional
    public Order createOrder(int orderId, List<Integer> bookIds, String customerName) {
        int totalSum = 0;
        List<Book> presentBooks = new ArrayList<>();

        for (int id : bookIds) {
            Book book = bookStorageRepository.getBook(id, false);

            // Проверка наличия книги
            if (book != null) {
                presentBooks.add(book);
            } else continue;

            totalSum += book.getPrice();
        }

        if (!presentBooks.isEmpty()) {
            Order order = new Order(orderId, presentBooks, totalSum, customerName);

            if (orderManagerRepository.getOrder(orderId, false) != null) {
                cancelOrder(orderId);
            }

            if (!(unitOfWork instanceof HibernateUnitOfWork))
                orderManagerRepository.addOrder(orderId, order);

            for (Book book : presentBooks) {

                if (book.getStatus() != BookStatus.FREE) {
                    requestManagerRepository.addRequest(book);
                } else {
                    book.setStatus(BookStatus.RESERVED, order);
                }

                if (!(unitOfWork instanceof HibernateUnitOfWork))
                    bookStorageRepository.updateBook(book);
            }

            if (unitOfWork instanceof HibernateUnitOfWork)
                orderManagerRepository.addOrder(orderId, order);
            return order;
        }
        throw new WrongId("указанных книг не существует");
    }

    @Transactional
    public Order cancelOrder(int orderId) {
        return unitOfWork.execute(() -> {
            Order order = orderManagerRepository.getOrder(orderId, false);

            if (order == null) throw new WrongId("заказ #" + orderId + "не существует");

            order.setStatus(OrderStatus.CANCELED);
            orderManagerRepository.updateOrder(order);

            for (Book book : order.getOrderedBooks()) {
                book.setStatus(BookStatus.FREE);

                bookStorageRepository.updateBook(book);
            }

            return order;
        });
    }

    @Transactional
    public Order changeOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = orderManagerRepository.getOrder(orderId, false);

        if (order == null) throw new WrongId("заказ #" + orderId + "не существует");

        if (newStatus == OrderStatus.COMPLETED) {
            for (Book book : order.getOrderedBooks()) {
                if (book.getStatus() != BookStatus.FREE && order.getId() != book.getOrderId()) {
                    throw new UnavailableAction(
                            "изменение статуса заказа",
                            "книга с артикулом #" + book.getId() + " недоступна"
                    );
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

        for (Book book : order.getOrderedBooks()) {
            book.setStatus(requiredBookStatus);

            bookStorageRepository
                    .updateBook(book);
        }

        return order;
    }
}
