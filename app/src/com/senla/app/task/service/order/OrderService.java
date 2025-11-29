package task.service.order;

import task.repository.OrderManagerRepository;
import task.repository.RequestManagerRepository;
import task.repository.StorageRepository;
import task.model.entity.Book;
import task.model.entity.Order;
import task.model.entity.Request;
import task.model.entity.status.BookStatus;
import task.model.entity.status.OrderStatus;

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

    public boolean createOrder(int orderId, List<Integer> bookIds, String customerName) {
        int totalSum = 0;
        List<Integer> presentBookIds = new ArrayList<>();

        for (int id : bookIds) {
            Book book = bookStorageRepository.getBook(id);

            // Проверка наличия книги
            if (book != null) {
                presentBookIds.add(id);
            } else continue;

            if (book.getStatus() != BookStatus.FREE) {;
                requestManagerRepository.addRequest(book.getTitle());

            } else book.setStatus(BookStatus.RESERVED, customerName);

            totalSum += book.getPrice();
        }

        if (!presentBookIds.isEmpty()) {
            Order order = new Order(orderId, presentBookIds, totalSum, customerName);

            if (orderManagerRepository.getOrder(orderId) != null) {
                cancelOrder(orderId);
            }

            orderManagerRepository.addOrder(orderId, order);
            return true;
        } else return false;

    }

    public void cancelOrder(int orderId) {
        Order order = orderManagerRepository.getOrder(orderId);
        order.setStatus(OrderStatus.CANCELED);

        for (int bookId : order.getOrderedBookIds()) {
            bookStorageRepository.getBook(bookId).setStatus(BookStatus.FREE);
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

        BookStatus requiredBookStatus = BookStatus.RESERVED;

        switch (newStatus) {
            case CANCELED -> requiredBookStatus = BookStatus.FREE;
            case COMPLETED -> requiredBookStatus = BookStatus.SOLD_OUT;
        }

        for (int bookId : order.getOrderedBookIds()) {
            bookStorageRepository.getBook(bookId).setStatus(requiredBookStatus);
        }

        return true;
    }
}
