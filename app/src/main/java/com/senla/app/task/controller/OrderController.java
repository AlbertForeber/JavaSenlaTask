package com.senla.app.task.controller;

import com.senla.annotation.InjectTo;
import com.senla.annotation.ui_qualifiers.Console;
import com.senla.app.task.model.entity.sortby.OrderSortBy;
import com.senla.app.task.model.entity.status.OrderStatus;
import com.senla.app.task.service.order.OrderQueryService;
import com.senla.app.task.service.order.OrderService;
import com.senla.app.task.service.unit_of_work.TransactionException;
import com.senla.app.task.utils.Colors;
import com.senla.app.task.utils.DateConverter;
import com.senla.app.task.view.IOHandler;
import com.senla.app.task.view.console.ConsoleIOHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class OrderController {

    @InjectTo
    private final OrderService orderService;

    @InjectTo
    private final OrderQueryService orderQueryService;

    @InjectTo(useImplementation = ConsoleIOHandler.class)
    private final IOHandler ioHandler;

    private static final Logger logger = LogManager.getLogger(OrderController.class);

    public OrderController(
            OrderService orderService,
            OrderQueryService orderQueryService,
            @Console IOHandler ioHandler
    ) {
        this.orderService = orderService;
        this.orderQueryService = orderQueryService;
        this.ioHandler = ioHandler;
    }

    public void createOrder() {
        ioHandler.showMessage(Colors.BLUE + "Введите ID заказа" + Colors.RESET);
        int orderId = Integer.parseInt(ioHandler.handleInput());

        ioHandler.showMessage(Colors.BLUE + "Введите имя заказчика" + Colors.RESET);
        String customerName = ioHandler.handleInput();

        ioHandler.showMessage(Colors.BLUE + "Введите количество книг в заказе" + Colors.RESET);
        int bookAmount = Integer.parseInt(ioHandler.handleInput());

        List<Integer> bookIds = new ArrayList<>();

        for (int i = 0; i < bookAmount; i++) {
            ioHandler.showMessage(Colors.BLUE + "Введите ID книги #" + (i + 1) + ":" + Colors.RESET);
            try {
                bookIds.add(Integer.parseInt(ioHandler.handleInput()));
            } catch (NumberFormatException e) {
                ioHandler.showMessage(Colors.YELLOW + "ID ДОЛЖЕН БЫТЬ ЧИСЛЕННЫМ ЗНАЧЕНИЕМ" + Colors.RESET);
            }
        }

        logger.info("Начало обработки добавления заказа");

        try {
            if (orderService.createOrder(orderId, bookIds, customerName)) {
                ioHandler.showMessage(Colors.YELLOW + "Заказ #" + orderId + " создан" + Colors.RESET);
            } else {
                logger.error("Ошибка при создании заказа #{}", orderId);
                ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ПРИ СОЗДАНИИ ЗАКАЗА #" + orderId + Colors.RESET);
            }
            logger.info("Заказ #{} успешно добавлен", orderId);
        } catch (TransactionException e) {
            logger.error(e.getMessage());
            ioHandler.showMessage(Colors.YELLOW + e.getMessage() + Colors.RESET);
        }
    }

    public void cancelOrder() {
        ioHandler.showMessage(Colors.BLUE + "Введите ID заказа" + Colors.RESET);
        int orderId = Integer.parseInt(ioHandler.handleInput());

        logger.info("Начало обработки отмены заказа");

        try {
            orderService.cancelOrder(orderId);
        } catch (TransactionException e) {
            logger.error(e.getMessage());
            ioHandler.showMessage(Colors.YELLOW + e.getMessage() + Colors.RESET);
        }

        logger.info("Заказ #{} успешно отменен", orderId);
        ioHandler.showMessage(Colors.YELLOW + "Заказ #" + orderId + " отменен" + Colors.RESET);
    }

    public void changeOrderStatus() {
        ioHandler.showMessage(Colors.BLUE + "Введите ID заказа" + Colors.RESET);
        int orderId = Integer.parseInt(ioHandler.handleInput());

        ioHandler.showMessage(Colors.BLUE + "Введите новый статус заказа (NEW/CANCELED/COMPLETED)" + Colors.RESET);
        String newStatusString = ioHandler.handleInput();

        logger.info("Начало обработки смены статуса заказа");

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

        try {
            if (orderService.changeOrderStatus(orderId, newStatus)) {
                logger.info("Статус заказа #{} успешно изменен", orderId);
                ioHandler.showMessage(Colors.YELLOW + "Статус заказа #" + orderId + " успешно изменен" + Colors.RESET);
            } else {
                logger.error("Ошибка изменения статуса заказа #{}", orderId);
                ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ИЗМЕНЕНИЯ СТАТУСА ЗАКАЗА #" + orderId + Colors.RESET);
            }
        } catch (TransactionException e) {
            logger.error(e.getMessage());
            ioHandler.showMessage(Colors.YELLOW + e.getMessage() + Colors.RESET);
        }
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

        logger.info("Начало обработки вывода отсортированных заказов");

        try {
            orderQueryService.getSorted(orderSortBy).stream()
                    .map(Object::toString)
                    .forEach(ioHandler::showMessage);
            logger.info("Вывод отсортированных заказов успешно завершен");
        } catch (Exception e) {
            logger.error("Ошибка при получении отсортированных заказов: {}", e.getMessage());
            ioHandler.showMessage(Colors.YELLOW + e.getMessage() + Colors.RESET);
        }
    }

    public void getCompletedOrdersInInterval() {
        ioHandler.showMessage("Введите дату начала интервала в формате (1.1.2025):");
        String input = ioHandler.handleInput();
        int[] dateFrom = DateConverter.getDateInArray(input);

        ioHandler.showMessage("Введите дату конца интервала в формате (1.1.2025):");
        input = ioHandler.handleInput();
        int[] dateTo = DateConverter.getDateInArray(input);

        logger.info("Начало обработки вывода выполненых в интервал заказов");

        if (dateFrom != null && dateTo != null) {
            try {
                ioHandler.showMessage(orderQueryService.getCompletedOrdersInInterval(dateFrom[2], dateFrom[1], dateFrom[0], dateTo[2], dateTo[1], dateTo[0], true).toString());
                logger.info("Вывод заказов выполненных за интервал успешно завершен");
            } catch (Exception e) {
                logger.error("Ошибка доступа к базе: {}", e.getMessage());
ioHandler.showMessage(Colors.YELLOW + e.getMessage() + Colors.RESET);
            }
        } else ioHandler.showMessage(Colors.YELLOW + "НЕВЕРНЫЙ ФОРМАТ ДАТЫ" + Colors.RESET);
    }

    public void getIncomeInInterval() {
        ioHandler.showMessage("Введите дату начала интервала в формате (1.1.2025):");
        String input = ioHandler.handleInput();
        int[] dateFrom = DateConverter.getDateInArray(input);

        ioHandler.showMessage("Введите дату конца интервала в формате (1.1.2025):");
        input = ioHandler.handleInput();
        int[] dateTo = DateConverter.getDateInArray(input);

        logger.info("Начало обработки вывода прибыли за интервал");

        if (dateFrom != null && dateTo != null) {
            try {
                ioHandler.showMessage(Long.toString(orderQueryService.getIncomeInInterval(dateFrom[2], dateFrom[1], dateFrom[0], dateTo[2], dateTo[1], dateTo[0])));
                logger.info("Вывод дохода за интервал успешно завершен");
            } catch (Exception e) {
                logger.error("Ошибка при выводе прибыли: {}", e.getMessage());
                ioHandler.showMessage(Colors.YELLOW + e.getMessage() + Colors.RESET);
            }
        } else ioHandler.showMessage(Colors.YELLOW + "НЕВЕРНЫЙ ФОРМАТ ДАТЫ" + Colors.RESET);
    }

    public void getOrderAmountInInterval() {
        ioHandler.showMessage("Введите дату начала интервала в формате (1.1.2025):");
        String input = ioHandler.handleInput();
        int[] dateFrom = DateConverter.getDateInArray(input);

        ioHandler.showMessage("Введите дату конца интервала в формате (1.1.2025):");
        input = ioHandler.handleInput();
        int[] dateTo = DateConverter.getDateInArray(input);

        logger.info("Начало обработки вывода количества выполненых в интервал заказов");

        if (dateFrom != null && dateTo != null) {
            try {
                ioHandler.showMessage(Integer.toString(orderQueryService.getOrderAmountInInterval(dateFrom[2], dateFrom[1], dateFrom[0], dateTo[2], dateTo[1], dateTo[0])));
                logger.info("Вывод количества заказов выполненных за интервал успешно завершен");
            } catch (Exception e) {
                logger.error("Ошибка при выводе количества заказов: {}", e.getMessage());
                ioHandler.showMessage(Colors.YELLOW + e.getMessage() + Colors.RESET);
            }
        } else ioHandler.showMessage(Colors.YELLOW + "НЕВЕРНЫЙ ФОРМАТ ДАТЫ" + Colors.RESET);
    }

    public void getOrderDetails() {
        ioHandler.showMessage(Colors.BLUE + "Введите ID заказа" + Colors.RESET);

        logger.info("Начало обработки вывода деталей заказа");

        try {
            int orderId = Integer.parseInt(ioHandler.handleInput());

            String result = orderQueryService.getOrderDetails(orderId);
            ioHandler.showMessage(Objects.requireNonNullElse(result, Colors.YELLOW + "ЗАКАЗ С УКАЗАННЫМ ID НЕ НАЙДЕН" + Colors.RESET));
            logger.info("Вывод деталей заказа успешно завершен");
        } catch (NumberFormatException e) {
            ioHandler.showMessage(Colors.YELLOW + "ID ДОЛЖЕН БЫТЬ ЧИСЛЕННЫМ ЗНАЧЕНИЕМ" + Colors.RESET);
        } catch (Exception e) {
            logger.error("Ошибка при выводе деталей: {}", e.getMessage());
            ioHandler.showMessage(Colors.YELLOW + e.getMessage() + Colors.RESET);
        }
    }
}