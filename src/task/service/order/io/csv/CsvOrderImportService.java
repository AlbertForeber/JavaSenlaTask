package task.service.order.io.csv;

import task.model.entity.Order;
import task.model.entity.status.OrderStatus;
import task.repository.OrderManagerRepository;
import task.repository.StorageRepository;
import task.service.order.io.OrderImportConstants;
import task.service.order.OrderService;
import task.service.order.io.OrderImportService;
import task.utils.DataConverter;
import task.utils.FileParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CsvOrderImportService implements OrderImportService {
    private final OrderManagerRepository orderManagerRepository;
    private final StorageRepository bookStorageRepository;
    private final OrderService orderService;

    public CsvOrderImportService(OrderManagerRepository orderManagerRepository, StorageRepository storageRepository, OrderService orderService) {
        this.orderManagerRepository = orderManagerRepository;
        this.bookStorageRepository = storageRepository;
        this.orderService = orderService;
    }

    public void importOrder(String fileName) throws IllegalArgumentException, IOException {
        Map<String, String> fields = FileParser.parseFile(fileName, OrderImportConstants.ALLOWED_FIELDS);
        FileParser.validateRequiredFields(fields, OrderImportConstants.REQUIRED_FIELDS);

        List<Integer> bookIds = Arrays.stream(fields.get("bookIds").split(",")).map(Integer::parseInt).toList();

        validateBookNames(bookIds);

        OrderStatus status = getVerifiedStatus(fields.get("status"), fields);

        if (status != OrderStatus.NEW) {
            Order order = new Order(
                    FileParser.parseNumericField(fields.get("id"), "id"),
                    bookIds,
                    FileParser.parseNumericField(fields.get("totalSum"), "totalSum"),
                    fields.get("customerName")
            );
            order.setStatus(status);

            if (fields.get("completionDate") != null) {
                int[] date = DataConverter.getDateInArray(fields.get("completionDate"));
                if (date == null) throw new IllegalArgumentException("Неверный формат даты завершения");
                order.setCompletionDate(date[2], date[1], date[0]);
            }

            orderManagerRepository.addOrder(order.getId(), order);
        }

        else {
            int id = FileParser.parseNumericField(fields.get("id"), "id");
            orderService.createOrder(
                    id,
                    bookIds,
                    fields.get("customerName")
            );

            orderManagerRepository
                    .getOrder(id)
                    .setTotalSum(FileParser.parseNumericField(fields.get("totalSum"), "totalSum"));
        }
    }

    private void validateBookNames(List<Integer> toValidate) {
        if (toValidate.isEmpty()) throw new IllegalArgumentException("Список книг не может быть пуст");

        for (int bookId : toValidate) {
            if (bookStorageRepository.getBook(bookId) == null)
                throw new IllegalArgumentException("Книга c ID '" + bookId + "' не найдена");
        }
    }

    private OrderStatus getVerifiedStatus(String status, Map<String, String> fields) {
        return switch (status) {
            case "NEW" -> {
                if (fields.get("completionDate") != null)
                    throw new IllegalArgumentException("Дата завершения для новых заказов не должна быть указана");

                yield OrderStatus.NEW;
            }
            case "COMPLETED" -> {
                if (fields.get("totalSum") == null)
                    throw new IllegalArgumentException("Статус COMPLETED может быть задан только с конечной стоимостью");

                if (fields.get("completionDate") == null)
                    throw new IllegalArgumentException("Статус COMPLETED может быть задан только с датой завершения");


                yield OrderStatus.COMPLETED;
            }
            case "CANCELED" -> {
                if (fields.get("totalSum") == null)
                    throw new IllegalArgumentException("Статус CANCELED может быть задан только с конечной стоимостью");

                yield OrderStatus.CANCELED;
            }
            default -> throw new IllegalArgumentException("Неверный статус: " + status + "'");
        };
    }
}
