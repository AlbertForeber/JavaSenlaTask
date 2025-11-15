package task.service.order.io.csv;

import task.model.entity.Order;
import task.model.entity.status.OrderStatus;
import task.repository.OrderManagerRepository;
import task.repository.StorageRepository;
import task.service.order.io.OrderImportConstants;
import task.service.order.OrderService;
import task.utils.DataConverter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvOrderImport {
    private final OrderManagerRepository orderManagerRepository;
    private final StorageRepository bookStorageRepository;
    private final OrderService orderService;

    public CsvOrderImport(OrderManagerRepository orderManagerRepository, StorageRepository storageRepository, OrderService orderService) {
        this.orderManagerRepository = orderManagerRepository;
        this.bookStorageRepository = storageRepository;
        this.orderService = orderService;
    }

    public void importOrder(String fileName) throws IllegalArgumentException, IOException {
        Map<String, String> fields = parseOrderFile(fileName);

        validateRequiredFields(fields);
        validateBookNames(List.of(fields.get("bookNames").split(",")));

        OrderStatus status = getVerifiedStatus(fields.get("status"), fields);

        if (status != OrderStatus.NEW) {
            Order order = new Order(
                    Integer.parseInt(fields.get("id")),
                    List.of(fields.get("bookNames").split(",")),
                    Integer.parseInt(fields.get("totalSum")),
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

        else orderService.createOrder(
                Integer.parseInt(fields.get("id")),
                List.of(fields.get("bookNames").split(",")),
                fields.get("customerName")
        );
    }

    private void validateBookNames(List<String> toValidate) {
        if (toValidate.isEmpty()) throw new IllegalArgumentException("Список книг не может быть пуст");

        for (String bookName : toValidate) {
            if (bookStorageRepository.getBook(bookName) == null)
                throw new IllegalArgumentException("Книга '" + bookName + "' не найдена");
        }
    }

    private OrderStatus getVerifiedStatus(String status, Map<String, String> fields) {
        return switch (status) {
            case "NEW" -> {
                if (fields.get("totalSum") != null)
                    throw new IllegalArgumentException("Итоговая сумма для новых заказов не должна быть указана");

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
            default -> throw new IllegalArgumentException("Неверный статус '" + status + "'");
        };
    }

    private Map<String, String> parseOrderFile(String fileName) throws IOException, IllegalArgumentException {
        Map<String, String> fields = new HashMap<>();

        try (BufferedReader bis = new BufferedReader(new FileReader(fileName))) {
            String curLine;

            while ((curLine = bis.readLine()) != null) {
                processLine(curLine, fields);
            }
        }
        return fields;
    }

    private void processLine(String line, Map<String, String> fields) throws IllegalArgumentException {
        List<String> keyValue = List.of(line.split(";"));
        if (keyValue.size() != 2) throw new IllegalArgumentException("Неверный формат строки '" + line + "'");

        String key = keyValue.getFirst().trim();
        String value = keyValue.getLast().trim();

        if (fields.containsKey(key)) throw new IllegalArgumentException("Дубликат поля '" + key + "'");
        if (!OrderImportConstants.ALLOWED_FIELDS.contains(key)) throw new IllegalArgumentException("Неизвестное поле '" + key + "'");

        fields.put(key, value);

    }

    // TODO вытащить отдельно
    // Fast-fail принцип, сразу выбрасываем ошибку (void + throw) вместо большого количества if-else блоков (bool + if-else)
    private void validateRequiredFields(Map<String, String> fields) {
        for (String field : OrderImportConstants.REQUIRED_FIELDS) {
            if (!fields.containsKey(field)) throw new IllegalArgumentException("Требуемое поле '" + field + "' не найдено");
        }
    }
}
