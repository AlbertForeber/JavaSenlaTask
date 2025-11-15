package task.controller;

import task.model.entity.sortby.OrderSortBy;
import task.model.entity.status.OrderStatus;
import task.service.order.OrderQueryService;
import task.service.order.OrderService;
import task.utils.Colors;
import task.utils.DataConverter;
import task.view.IOHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderController extends BaseController {
    OrderService orderService;
    OrderQueryService orderQueryService;

    public OrderController(
            OrderQueryService orderQueryService,
            OrderService orderService,
            IOHandler ioHandler
            ) {
        super(ioHandler);
        this.orderQueryService = orderQueryService;
        this.orderService = orderService;
    }

    public void createOrder() {
        ioHandler.showMessage(Colors.BLUE + "Введите номер заказа" + Colors.RESET);
        int orderId = Integer.parseInt(ioHandler.handleInput());

        ioHandler.showMessage(Colors.BLUE + "Введите имя заказчика" + Colors.RESET);
        String customerName = ioHandler.handleInput();

        ioHandler.showMessage(Colors.BLUE + "Введите количество книг в заказе" + Colors.RESET);
        int bookAmount = Integer.parseInt(ioHandler.handleInput());

        List<String> bookNames = new ArrayList<>();

        for (int i = 0; i < bookAmount; i ++) {
            ioHandler.showMessage(Colors.BLUE + "Введите название книги #" + (i + 1) + ":" + Colors.RESET);
            bookNames.add(ioHandler.handleInput());
        }

        if (orderService.createOrder(orderId, bookNames, customerName)) {
            ioHandler.showMessage(Colors.YELLOW + "ЗАКАЗ #" + orderId + " СОЗДАН" + Colors.RESET);
        } else ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ПРИ СОЗДАНИИ ЗАКАЗА #" + orderId + Colors.RESET);


    }

    public void cancelOrder() {
        ioHandler.showMessage(Colors.BLUE + "Введите номер заказа" + Colors.RESET);
        int orderId = Integer.parseInt(ioHandler.handleInput());

        orderService.cancelOrder(orderId);

        ioHandler.showMessage(Colors.YELLOW + "ЗАКАЗ #" + orderId + " ОТМЕНЕН" + Colors.RESET);
    }

    public void changeOrderStatus() {
        ioHandler.showMessage(Colors.BLUE + "Введите номер заказа" + Colors.RESET);
        int orderId = Integer.parseInt(ioHandler.handleInput());

        ioHandler.showMessage(Colors.BLUE + "Введите новый статус заказа (new/canceled/completed)" + Colors.RESET);
        String newStatusString = ioHandler.handleInput();

        OrderStatus newStatus = switch (newStatusString) {
            case "new" -> OrderStatus.NEW;
            case "canceled" -> OrderStatus.CANCELED;
            case "completed" -> OrderStatus.COMPLETED;
            default -> null;
        };

        if (newStatus == null) {
            ioHandler.showMessage(Colors.YELLOW + "Введен неверный статус" + Colors.RESET);
            return;
        }

        if (orderService.changeOrderStatus(orderId, newStatus)) {
            ioHandler.showMessage(Colors.YELLOW + "СТАТУС ЗАКАЗА #" + orderId + " УСПЕШНО ИЗМЕНЕН");
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
            ioHandler.showMessage("Выбран неверный пункт меню");
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
        ioHandler.showMessage(Colors.BLUE + "Введите номер заказа" + Colors.RESET);
        int orderId = Integer.parseInt(ioHandler.handleInput());

        ioHandler.showMessage(orderQueryService.getOrderDetails(orderId));
    }
}