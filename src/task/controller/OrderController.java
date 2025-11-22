package task.controller;

import task.model.entity.sortby.OrderSortBy;
import task.model.entity.status.OrderStatus;
import task.service.order.OrderQueryService;
import task.service.order.OrderService;
import task.service.order.io.OrderExportService;
import task.service.order.io.OrderImportService;
import task.utils.Colors;
import task.utils.DataConverter;
import task.view.IOHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderController extends BaseController {
    private final OrderService orderService;
    private final OrderQueryService orderQueryService;
    private final OrderImportService orderImportService;
    private final OrderExportService orderExportService;

    public OrderController(
            OrderQueryService orderQueryService,
            OrderService orderService,
            OrderImportService orderImportService,
            OrderExportService orderExportService,
            IOHandler ioHandler
            ) {
        super(ioHandler);
        this.orderQueryService = orderQueryService;
        this.orderService = orderService;
        this.orderImportService = orderImportService;
        this.orderExportService = orderExportService;
    }

    public void createOrder() {
        ioHandler.showMessage(Colors.BLUE + "Введите ID заказа" + Colors.RESET);
        int orderId = Integer.parseInt(ioHandler.handleInput());

        ioHandler.showMessage(Colors.BLUE + "Введите имя заказчика" + Colors.RESET);
        String customerName = ioHandler.handleInput();

        ioHandler.showMessage(Colors.BLUE + "Введите количество книг в заказе" + Colors.RESET);
        int bookAmount = Integer.parseInt(ioHandler.handleInput());

        List<Integer> bookIds = new ArrayList<>();

        for (int i = 0; i < bookAmount; i ++) {
            ioHandler.showMessage(Colors.BLUE + "Введите ID книги #" + (i + 1) + ":" + Colors.RESET);
            try {
                bookIds.add(Integer.parseInt(ioHandler.handleInput()));
            } catch (NumberFormatException e) {
                ioHandler.showMessage(Colors.YELLOW + "ID ДОЛЖЕН БЫТЬ ЧИСЛЕННЫМ ЗНАЧЕНИЕМ" + Colors.RESET);
            }
        }

        if (orderService.createOrder(orderId, bookIds, customerName)) {
            ioHandler.showMessage(Colors.YELLOW + "Заказ #" + orderId + " создан" + Colors.RESET);
        } else ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ПРИ СОЗДАНИИ ЗАКАЗА #" + orderId + Colors.RESET);


    }

    public void cancelOrder() {
        ioHandler.showMessage(Colors.BLUE + "Введите ID заказа" + Colors.RESET);
        int orderId = Integer.parseInt(ioHandler.handleInput());

        orderService.cancelOrder(orderId);

        ioHandler.showMessage(Colors.YELLOW + "Заказ #" + orderId + " отменен" + Colors.RESET);
    }

    public void changeOrderStatus() {
        ioHandler.showMessage(Colors.BLUE + "Введите ID заказа" + Colors.RESET);
        int orderId = Integer.parseInt(ioHandler.handleInput());

        ioHandler.showMessage(Colors.BLUE + "Введите новый статус заказа (NEW/CANCELED/COMPLETED)" + Colors.RESET);
        String newStatusString = ioHandler.handleInput();

        OrderStatus newStatus = switch (newStatusString) {
            case "NEW" -> OrderStatus.NEW;
            case "CANCELED" -> OrderStatus.CANCELED;
            case "COMPLETED" -> OrderStatus.COMPLETED;
            default -> null;
        };

        if (newStatus == null) {
            ioHandler.showMessage(Colors.YELLOW + "ВВЕДЕН НЕВЕРНЫЙ СТАТУС" + Colors.RESET);
            return;
        }

        if (orderService.changeOrderStatus(orderId, newStatus)) {
            ioHandler.showMessage(Colors.YELLOW + "Статус заказа #" + orderId + " успешно изменен" + Colors.RESET);
        } else ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ИЗМЕНЕНИЯ СТАТУСА ЗАКАЗА #" + orderId + Colors.RESET);
    }

    public void getSorted() {
        ioHandler.showMessage(Colors.BLUE + "Выберите вариант сортировки" + Colors.RESET);

        ioHandler.showMessage("1. По дате выполнения");
        ioHandler.showMessage("2. По статусу");
        ioHandler.showMessage("3. По цене");
        ioHandler.showMessage("4. Без сортировки");

        String chosen = ioHandler.handleInput();

        OrderSortBy orderSortBy = switch (chosen) {
            case "1" -> OrderSortBy.COMPLETION_DATE;
            case "2" -> OrderSortBy.STATUS;
            case "3" -> OrderSortBy.PRICE;
            case "4" -> OrderSortBy.NO_SORT;
            default -> null;
        };

        if (orderSortBy == null) {
            ioHandler.showMessage(Colors.YELLOW +  "ВЫБРАН НЕВЕРНЫЙ ПУНКТ МЕНЮ" + Colors.RESET);
            return;
        }

        orderQueryService.getSorted(orderSortBy).stream()
                .map(Object::toString)
                .forEach(ioHandler::showMessage);
    }

    public void getCompletedOrdersInInterval() {
        ioHandler.showMessage("Введите дату начала интервала в формате (1.1.2025):");
        String input = ioHandler.handleInput();
        int[] dateFrom = DataConverter.getDateInArray(input);

        ioHandler.showMessage("Введите дату конца интервала в формате (1.1.2025):");
        input = ioHandler.handleInput();
        int[] dateTo = DataConverter.getDateInArray(input);

        if (dateFrom != null && dateTo != null) {
            ioHandler.showMessage(orderQueryService.getCompletedOrdersInInterval(dateFrom[2], dateFrom[1], dateFrom[0], dateTo[2], dateTo[1], dateTo[0]).toString());
        } else ioHandler.showMessage(Colors.YELLOW + "НЕВЕРНЫЙ ФОРМАТ ДАТЫ" + Colors.RESET);

    }

    public void getIncomeInInterval() {
        ioHandler.showMessage("Введите дату начала интервала в формате (1.1.2025):");
        String input = ioHandler.handleInput();
        int[] dateFrom = DataConverter.getDateInArray(input);

        ioHandler.showMessage("Введите дату конца интервала в формате (1.1.2025):");
        input = ioHandler.handleInput();
        int[] dateTo = DataConverter.getDateInArray(input);

        if (dateFrom != null && dateTo != null) {
            ioHandler.showMessage(Long.toString(orderQueryService.getIncomeInInterval(dateFrom[2], dateFrom[1], dateFrom[0], dateTo[2], dateTo[1], dateTo[0])));
        } else ioHandler.showMessage(Colors.YELLOW + "НЕВЕРНЫЙ ФОРМАТ ДАТЫ" + Colors.RESET);
    }

    public void getOrderAmountInInterval() {
        ioHandler.showMessage("Введите дату начала интервала в формате (1.1.2025):");
        String input = ioHandler.handleInput();
        int[] dateFrom = DataConverter.getDateInArray(input);

        ioHandler.showMessage("Введите дату конца интервала в формате (1.1.2025):");
        input = ioHandler.handleInput();
        int[] dateTo = DataConverter.getDateInArray(input);

        if (dateFrom != null && dateTo != null) {
            ioHandler.showMessage(Integer.toString(orderQueryService.getOrderAmountInInterval(dateFrom[2], dateFrom[1], dateFrom[0], dateTo[2], dateTo[1], dateTo[0])));
        } else ioHandler.showMessage(Colors.YELLOW + "НЕВЕРНЫЙ ФОРМАТ ДАТЫ" + Colors.RESET);
    }

    public void getOrderDetails() {
        ioHandler.showMessage(Colors.BLUE + "Введите ID заказа" + Colors.RESET);

        try {
            int orderId = Integer.parseInt(ioHandler.handleInput());
            ioHandler.showMessage(orderQueryService.getOrderDetails(orderId));
        } catch (NumberFormatException e) {
            ioHandler.showMessage(Colors.YELLOW + "ID ДОЛЖЕН БЫТЬ ЧИСЛЕННЫМ ЗНАЧЕНИЕМ" + Colors.RESET);
        }
    }

    public void importOrder() {
        ioHandler.showMessage(Colors.BLUE + "Введите путь к файлу" + Colors.RESET);
        String fileName = ioHandler.handleInput();

        try {
            orderImportService.importOrder(fileName);
            ioHandler.showMessage(Colors.YELLOW + "Заказ '" + fileName + "' успешно импортирован" + Colors.RESET);
        } catch (FileNotFoundException e) {
            ioHandler.showMessage(Colors.YELLOW + "ФАЙЛ '" + fileName + "' НЕ НАЙДЕН" + Colors.RESET);
        } catch (IOException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ВО ВРЕМЯ ЧТЕНИЯ ДОКУМЕНТА" + Colors.RESET);
        } catch (IllegalArgumentException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА: " + e.getMessage() + Colors.RESET);
        }
    }

    public void exportOrder() {
        ioHandler.showMessage(Colors.BLUE + "Введите ID заказа" + Colors.RESET);

        try {
            int orderId = Integer.parseInt(ioHandler.handleInput());

            ioHandler.showMessage(Colors.BLUE + "Введите путь для файла (Без имени файла, с разделителем на конце)" + Colors.RESET);
            ioHandler.showMessage(Colors.BLUE + "Пример: ./dir1/dir2/dir3/ (для Linux)" + Colors.RESET);

            orderExportService.exportOrder(orderId, ioHandler.handleInput());
            ioHandler.showMessage(Colors.YELLOW + "Заказ с ID: '" + orderId + "' успешно экспортирован" + Colors.RESET);

        } catch (NumberFormatException e) {
            ioHandler.showMessage(Colors.YELLOW + "ID ДОЛЖЕН БЫТЬ ЧИСЛЕННЫМ ЗНАЧЕНИЕМ" + Colors.RESET);
        } catch (IOException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ВО ВРЕМЯ СОЗДАНИЯ ДОКУМЕНТА" + Colors.RESET);
        } catch (IllegalArgumentException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА: " + e.getMessage() + Colors.RESET);
        }
    }
}