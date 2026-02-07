package com.senla.app.controller;

import com.senla.annotation.InjectTo;
import com.senla.annotation.ui_qualifiers.Console;
import com.senla.app.model.entity.sortby.RequestSortBy;
import com.senla.app.service.request.RequestQueryService;
import com.senla.app.service.request.RequestService;
import com.senla.app.service.unit_of_work.TransactionException;
import com.senla.app.utils.Colors;
import com.senla.app.view.IOHandler;
import com.senla.app.view.console.ConsoleIOHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class RequestController {

    @InjectTo
    private final RequestService requestService;

    @InjectTo
    private final RequestQueryService requestQueryService;

    @InjectTo(useImplementation = ConsoleIOHandler.class)
    private final IOHandler ioHandler;

    private static final Logger logger = LogManager.getLogger(RequestController.class);

    public RequestController(
            RequestService requestService,
            RequestQueryService requestQueryService,
            @Console IOHandler ioHandler) {
        this.requestService = requestService;
        this.requestQueryService = requestQueryService;
        this.ioHandler = ioHandler;
    }

    public void createRequest() {
        ioHandler.showMessage(Colors.BLUE + "Введите ID книги на которую создается запрос:" + Colors.RESET);
        int bookId = 0;

        try {
            bookId = Integer.parseInt(ioHandler.handleInput());
        } catch (NumberFormatException e) {
            ioHandler.showMessage(Colors.YELLOW + "ID ДОЛЖЕН БЫТЬ ЧИСЛЕННЫМ ЗНАЧЕНИЕМ" + Colors.RESET);
            return;
        }

        logger.info("Начало обработки добавления запроса");

        try {
            requestService.createRequest(bookId);
            logger.info("Запрос на книгу #{} успешно создан", bookId);
        } catch (TransactionException e) {
            logger.error(e.getMessage());
            ioHandler.showMessage(Colors.YELLOW + e.getMessage() + Colors.RESET);
        } catch (Exception e) {
            logger.error("Книга недоступна");
            ioHandler.showMessage(Colors.YELLOW + "КНИГА НЕДОСТУПНА" + Colors.RESET);
        }
    }

    public void getSorted() {
        ioHandler.showMessage(Colors.BLUE + "Выберите вариант сортировки" + Colors.RESET);

        ioHandler.showMessage("1. По названию книги");
        ioHandler.showMessage("2. По количеству");
        ioHandler.showMessage("3. Без сортировки");

        String chosen = ioHandler.handleInput();

        RequestSortBy requestSortBy = switch (chosen) {
            case "1" -> RequestSortBy.BOOK_NAME;
            case "2" -> RequestSortBy.AMOUNT;
            case "3" -> RequestSortBy.NO_SORT;
            default -> null;
        };

        if (requestSortBy == null) {
            ioHandler.showMessage(Colors.YELLOW + "Выбран неверный вариант сортировки" + Colors.RESET);
            return;
        }

        logger.info("Начало обработки получения отсортированных запросов");

        try {
            requestQueryService.getSorted(requestSortBy).stream()
                    .map(Object::toString)
                    .forEach(ioHandler::showMessage);
            logger.info("Вывод отсортированных запросов успешно завершен");
        } catch (Exception e) {
            logger.error("Ошибка при получении запросов {}", e.getMessage());
            ioHandler.showMessage(Colors.YELLOW + e.getMessage() + Colors.RESET);
        }
    }
}