package com.senla.app.controller;

import com.senla.annotation.InjectTo;
import com.senla.annotation.ui_qualifiers.Console;
import com.senla.app.model.entity.sortby.BookSortBy;
import com.senla.app.service.storage.StorageQueryService;
import com.senla.app.service.storage.StorageService;
import com.senla.app.service.unit_of_work.TransactionException;
import com.senla.app.utils.Colors;
import com.senla.app.utils.DateConverter;
import com.senla.app.view.IOHandler;
import com.senla.app.view.console.ConsoleIOHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class StorageController {

    @InjectTo(configurable = true)
    private final StorageService storageService;

    @InjectTo(configurable = true)
    private final StorageQueryService storageQueryService;

    @InjectTo(useImplementation = ConsoleIOHandler.class)
    private final IOHandler ioHandler;

    private static final Logger logger = LogManager.getLogger(StorageController.class);

    public StorageController(
            StorageService storageService,
            StorageQueryService storageQueryService,
            @Console IOHandler ioHandler) {
        this.storageService = storageService;
        this.storageQueryService = storageQueryService;
        this.ioHandler = ioHandler;
    }

    public void writeOffBook() {
        ioHandler.showMessage(Colors.BLUE + "Введите ID книги:" + Colors.RESET);
        int bookId = 0;

        try {
            bookId = Integer.parseInt(ioHandler.handleInput());
        } catch (NumberFormatException e) {
            ioHandler.showMessage(Colors.YELLOW + "ID ДОЛЖЕН БЫТЬ ЧИСЛЕННЫМ ЗНАЧЕНИЕМ" + Colors.RESET);
            return;
        }

        logger.info("Начало обработки списания книги");

        try {
            storageService.writeOffBook(bookId);
            logger.info("Книга #{} успешно списана", bookId);
        } catch (TransactionException e) {
            logger.error(e.getMessage());
            ioHandler.showMessage(Colors.YELLOW + e.getMessage() + Colors.RESET);
        } catch (Exception e) {
            logger.error("Книга недоступна: {}", e.getMessage());
            ioHandler.showMessage(Colors.YELLOW + "КНИГА НЕДОСТУПНА" + Colors.RESET + e.getMessage());
        }
    }

    public void addBookToStorage() {
        ioHandler.showMessage(Colors.BLUE + "Введите ID книги:" + Colors.RESET);
        int bookId = 0;

        try {
            bookId = Integer.parseInt(ioHandler.handleInput());
        } catch (NumberFormatException e) {
            ioHandler.showMessage(Colors.YELLOW + "ID ДОЛЖЕН БЫТЬ ЧИСЛЕННЫМ ЗНАЧЕНИЕМ" + Colors.RESET);
            return;
        }

        logger.info("Начало обработки добавления книги");

        try {
            storageService.addBookToStorage(bookId);
            logger.info("Книга #{} успешно добавлена", bookId);
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

        ioHandler.showMessage("1. По названию");
        ioHandler.showMessage("2. По дате поставки");
        ioHandler.showMessage("3. По дате публикации");
        ioHandler.showMessage("4. По цене");
        ioHandler.showMessage("5. По наличию");
        ioHandler.showMessage("6. Без сортировки");

        String chosen = ioHandler.handleInput();

        BookSortBy bookSortBy = switch (chosen) {
            case "1" -> BookSortBy.TITLE;
            case "2" -> BookSortBy.ADMISSION_DATE;
            case "3" -> BookSortBy.PUBLICATION_DATE;
            case "4" -> BookSortBy.PRICE;
            case "5" -> BookSortBy.AVAILABILITY;
            case "6" -> BookSortBy.NO_SORT;
            default -> null;
        };

        if (bookSortBy == null) {
            ioHandler.showMessage(Colors.YELLOW + "ВЫБРАН НЕВЕРНЫЙ ПУНКТ МЕНЮ" + Colors.RESET);
            return;
        }

        logger.info("Начало обработки вывода отсортированных книг");

        try {
            storageQueryService.getSorted(bookSortBy).stream()
                    .map(Object::toString)
                    .forEach(ioHandler::showMessage);
            logger.info("Вывод отсортированных книг успешно завершен");
        } catch (Exception e) {
            logger.error("Ошибка при получении книг: {}", e.getMessage());
            ioHandler.showMessage(Colors.YELLOW + e.getMessage() + Colors.RESET);
        }
    }

    public void getHardToSell() {
        ioHandler.showMessage(Colors.BLUE + "Введите дату в формате (1.1.2025):" + Colors.RESET);
        String input = ioHandler.handleInput();

        int[] date = DateConverter.getDateInArray(input);

        if (date != null) {
            try {
                logger.info("Начало обработки получения залежавшихся книг");
                storageQueryService.getHardToSell(date[2], date[1], date[0]).forEach(System.out::println);
                logger.info("Вывод залежавшихся книг успешно завершен");
            } catch (Exception e) {
                logger.error("Ошибка при получении малопродаваемых книг: {}", e.getMessage());
                ioHandler.showMessage(Colors.YELLOW + e.getMessage() + Colors.RESET);
            }
        } else ioHandler.showMessage(Colors.YELLOW + "НЕВЕРНЫЙ ФОРМАТ ДАТЫ" + Colors.RESET);
    }

    public void getBookDescription() {
        ioHandler.showMessage(Colors.BLUE + "Введите ID книги:" + Colors.RESET);
        int bookId = 0;

        try {
            bookId = Integer.parseInt(ioHandler.handleInput());
        } catch (NumberFormatException e) {
            ioHandler.showMessage(Colors.YELLOW + "ID ДОЛЖЕН БЫТЬ ЧИСЛЕННЫМ ЗНАЧЕНИЕМ" + Colors.RESET);
            return;
        }

        logger.info("Начало обработки получения описания книги");

        try {
            ioHandler.showMessage(storageQueryService.getBookDescription(bookId));
            logger.info("Вывод описания книги успешно завершен");
        } catch (Exception e) {
            logger.error("Ошибка доступа к базе: {}", e.getMessage());
            ioHandler.showMessage(Colors.YELLOW + e.getMessage() + Colors.RESET);
        }
    }
}
