package com.senla.app.task.service.order;

import com.senla.annotation.InjectTo;
import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
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
import com.senla.app.task.service.unit_of_work.UnitOfWork;
import com.senla.app.task.service.unit_of_work.implementations.HibernateUnitOfWork;
import org.springframework.stereotype.Service;

import java.util.*;


// Сервис, а не фасад, так как содержит бизнес-логику, а не простые вызовы готовых методов
// + все методы отвечают за одну предметную область.

@Service
public class OrderService {

    @InjectTo(useImplementation = DbOrderManagerRepository.class)
    private final OrderManagerRepository orderManagerRepository;

    @InjectTo(useImplementation = DbStorageRepository.class)
    private final StorageRepository bookStorageRepository;

    @InjectTo(useImplementation = DbRequestManagerRepository.class)
    private final RequestManagerRepository requestManagerRepository;

    @InjectTo(useImplementation = HibernateUnitOfWork.class)
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

    public boolean createOrder(int orderId, List<Integer> bookIds, String customerName) {
        return unitOfWork.execute(() -> {
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
                return true;
            }
            return false;
        });
    }

    public void cancelOrder(int orderId) {
        unitOfWork.executeVoid(() -> {
            Order order = orderManagerRepository.getOrder(orderId, false);
            order.setStatus(OrderStatus.CANCELED);

            orderManagerRepository.updateOrder(order);

            for (Book book : order.getOrderedBooks()) {
                book.setStatus(BookStatus.FREE);

                bookStorageRepository.updateBook(book);
            }
        });
    }

    public boolean changeOrderStatus(int orderId, OrderStatus newStatus) {
        return unitOfWork.execute(() -> {
            Order order = orderManagerRepository.getOrder(orderId, false);

            if (newStatus == OrderStatus.COMPLETED) {
                for (Book book : order.getOrderedBooks()) {
                    if (book.getStatus() != BookStatus.FREE && order.getId() != book.getOrderId()) {
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

            for (Book book : order.getOrderedBooks()) {
                book.setStatus(requiredBookStatus);

                bookStorageRepository
                        .updateBook(book);
            }

            return true;
        });
    }
}
