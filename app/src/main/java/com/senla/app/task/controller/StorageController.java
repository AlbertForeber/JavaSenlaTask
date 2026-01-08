package com.senla.app.task.controller;

import com.senla.annotation.InjectTo;
import com.senla.app.task.model.entity.sortby.BookSortBy;
import com.senla.app.task.service.storage.StorageQueryService;
import com.senla.app.task.service.storage.StorageService;
import com.senla.app.task.service.storage.io.StorageExportService;
import com.senla.app.task.service.storage.io.StorageImportService;
import com.senla.app.task.service.storage.io.csv.CsvStorageExportService;
import com.senla.app.task.service.storage.io.csv.CsvStorageImportService;
import com.senla.app.task.service.unit_of_work.TransactionException;
import com.senla.app.task.utils.Colors;
import com.senla.app.task.utils.DataConverter;
import com.senla.app.task.view.IOHandler;
import com.senla.app.task.view.console.ConsoleIOHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;

public class StorageController extends BaseController {

    @InjectTo(configurable = true)
    private StorageService storageService;

    @InjectTo(configurable = true)
    private StorageQueryService storageQueryService;

    @InjectTo(useImplementation = CsvStorageImportService.class)
    private StorageImportService storageImportService;

    @InjectTo(useImplementation = CsvStorageExportService.class)
    private StorageExportService storageExportService;

    @InjectTo(useImplementation = ConsoleIOHandler.class)
    private IOHandler ioHandler;

    private static final Logger logger = LogManager.getLogger(StorageController.class);

    public StorageController() {
        super();
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
        } catch (Exception e) {
            logger.error("Ошибка доступа к базе при получении книг: {}", e.getMessage());
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ДОСТУПА К БАЗЕ: " + e.getMessage() + Colors.RESET);
        }
    }

    public void getHardToSell() {
        ioHandler.showMessage(Colors.BLUE + "Введите дату в формате (1.1.2025):" + Colors.RESET);
        String input = ioHandler.handleInput();

        int[] date = DataConverter.getDateInArray(input);

        if (date != null) {
            try {
                logger.info("Начало обработки получения малопродаваемых книг");
                storageQueryService.getHardToSell(date[2], date[1], date[0]).forEach(System.out::println);
            } catch (Exception e) {
                logger.error("Ошибка доступа к базе при получении малопродаваемых книг: {}", e.getMessage());
                ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ДОСТУПА К БАЗЕ: " + e.getMessage() + Colors.RESET);
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
        } catch (Exception e) {
            logger.error("Ошибка доступа к базе: {}", e.getMessage());
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ДОСТУПА К БАЗЕ: " + e.getMessage() + Colors.RESET);
        }
    }

    public void importBook() {
        ioHandler.showMessage(Colors.BLUE + "Введите путь к файлу" + Colors.RESET);
        String fileName = ioHandler.handleInput();

        try {
            storageImportService.importBook(fileName);
            ioHandler.showMessage(Colors.YELLOW + "Книга '" + fileName + "' успешно импортирована" + Colors.RESET);
        } catch (FileNotFoundException e) {
            ioHandler.showMessage(Colors.YELLOW + "ФАЙЛ '" + fileName + "' НЕ НАЙДЕН" + Colors.RESET);
        } catch (IOException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ВО ВРЕМЯ ЧТЕНИЯ ДОКУМЕНТА" + Colors.RESET);
        } catch (IllegalArgumentException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА: " + e.getMessage() + Colors.RESET);
        }
    }

    public void exportBook() {
        ioHandler.showMessage(Colors.BLUE + "Введите ID книги" + Colors.RESET);

        try {
            int requestId = Integer.parseInt(ioHandler.handleInput());

            ioHandler.showMessage(Colors.BLUE + "Введите путь для файла (Без имени файла, с разделителем на конце)" + Colors.RESET);
            ioHandler.showMessage(Colors.BLUE + "Пример: ./dir1/dir2/dir3/ (для Linux)" + Colors.RESET);

            storageExportService.exportBook(requestId, ioHandler.handleInput());
            ioHandler.showMessage(Colors.YELLOW + "Книга с ID: '" + requestId + "' успешно экспортирована" + Colors.RESET);
        } catch (NumberFormatException e) {
            ioHandler.showMessage(Colors.YELLOW + "ID ДОЛЖЕН БЫТЬ ЧИСЛЕННЫМ ЗНАЧЕНИЕМ" + Colors.RESET);
        } catch (IOException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ВО ВРЕМЯ СОЗДАНИЯ ДОКУМЕНТА" + Colors.RESET);
        } catch (IllegalArgumentException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА: " + e.getMessage() + Colors.RESET);
        }
    }
}
