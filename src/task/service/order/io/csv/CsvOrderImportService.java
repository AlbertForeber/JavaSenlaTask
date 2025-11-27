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
import java.util.ArrayList;
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
        Map<String, ArrayList<String>> fields = FileParser.parseFile(
                fileName, OrderImportConstants.ALLOWED_FIELDS, OrderImportConstants.REQUIRED_FIELDS
        );

        for (int i = 0; i < fields.get("id").size(); i ++) {
            List<Integer> bookIds = Arrays.stream(fields.get("bookIds").get(i).split(",")).map(Integer::parseInt).toList();
            validateBookNames(bookIds);
            OrderStatus status = getVerifiedStatus(fields.get("status").get(i), fields, i);

            if (status != OrderStatus.NEW) {
                Order order = new Order(
                        FileParser.parseNumericField(fields.get("id").get(i), "id"),
                        bookIds,
                        FileParser.parseNumericField(fields.get("totalSum").get(i), "totalSum"),
                        fields.get("customerName").get(i)
                );

                // Перед заменой отменяем предыдущий заказ
                if (orderManagerRepository.getOrder(order.getId()) != null)
                    orderService.cancelOrder(order.getId());


                order.setStatus(status);

                if (fields.get("completionDate") != null
                        && !fields.get("completionDate").get(i).trim().isEmpty()) {

                    int[] date = DataConverter.getDateInArray(fields.get("completionDate").get(i));
                    if (date == null) throw new IllegalArgumentException("Неверный формат даты завершения");
                    order.setCompletionDate(date[2], date[1], date[0]);
                }

                orderManagerRepository.addOrder(order.getId(), order);
            } else {
                int id = FileParser.parseNumericField(fields.get("id").get(i), "id");

                orderService.createOrder(
                        id,
                        bookIds,
                        fields.get("customerName").get(i)
                );

                if (fields.get("totalSum") != null &&
                        !fields.get("totalSum").get(i).trim().isEmpty()) {
                    orderManagerRepository
                            .getOrder(id)
                            .setTotalSum(FileParser.parseNumericField(fields.get("totalSum").get(i), "totalSum"));
                }
            }
        }
    }

    private void validateBookNames(List<Integer> toValidate) {
        if (toValidate.isEmpty()) throw new IllegalArgumentException("Список книг не может быть пуст");

        for (int bookId : toValidate) {
            if (bookStorageRepository.getBook(bookId) == null)
                throw new IllegalArgumentException("Книга c ID '" + bookId + "' не найдена");
        }
    }

    private OrderStatus getVerifiedStatus(String status, Map<String, ArrayList<String>> fields, int index) {
        return switch (status) {
            case "NEW" -> {
                if (fields.get("completionDate") != null &&
                        !fields.get("completionDate").get(index).trim().isEmpty())
                    throw new IllegalArgumentException("Дата завершения для новых заказов не должна быть указана");

                yield OrderStatus.NEW;
            }
            case "COMPLETED" -> {
                if (fields.get("totalSum") == null ||
                        fields.get("totalSum").get(index).trim().isEmpty())
                    throw new IllegalArgumentException("Статус COMPLETED может быть задан только с конечной стоимостью");

                if (fields.get("completionDate") == null ||
                        fields.get("totalSum").get(index).trim().isEmpty())
                    throw new IllegalArgumentException("Статус COMPLETED может быть задан только с датой завершения");


                yield OrderStatus.COMPLETED;
            }
            case "CANCELED" -> {
                if (fields.get("totalSum") == null ||
                        fields.get("totalSum").get(index).trim().isEmpty())
                    throw new IllegalArgumentException("Статус CANCELED может быть задан только с конечной стоимостью");

                yield OrderStatus.CANCELED;
            }
            default -> throw new IllegalArgumentException("Неверный статус: " + status + "'");
        };
    }
}
