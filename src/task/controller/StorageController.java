package task.controller;

import task.model.entity.sortby.BookSortBy;
import task.service.storage.StorageQueryService;
import task.service.storage.StorageService;
import task.utils.Colors;
import task.utils.DataConverter;
import task.view.IOHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StorageController extends BaseController {
    StorageService storageService;
    StorageQueryService storageQueryService;

    public StorageController(
            StorageQueryService storageQueryService,
            StorageService storageService,
            IOHandler ioHandler
    ) {
        super(ioHandler);
        this.storageQueryService = storageQueryService;
        this.storageService = storageService;
    }

    public void writeOffBook() {
        ioHandler.showMessage(Colors.BLUE + "Введите название книги:" + Colors.RESET);

        try {
            storageService.writeOffBook(ioHandler.handleInput());
        } catch (Exception e) { ioHandler.showMessage(Colors.YELLOW + "КНИГА НЕДОСТУПНА" + Colors.RESET); }

    }

    public void addBookToStorage() {
        ioHandler.showMessage(Colors.BLUE + "Введите название книги:" + Colors.RESET);

        try {
            storageService.addBookToStorage(ioHandler.handleInput());
        } catch (Exception e) { ioHandler.showMessage(Colors.YELLOW + "КНИГА НЕДОСТУПНА" + Colors.RESET); }
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
            ioHandler.showMessage(Colors.YELLOW + "Выбран неверный пункт меню" + Colors.RESET);
            return;
        }

        storageQueryService.getSorted(bookSortBy).stream()
                .map(Object::toString)
                .forEach(ioHandler::showMessage);
    }

    public void getHardToSell() {
        ioHandler.showMessage(Colors.BLUE + "Введите дату в формате (1.1.2025):" + Colors.RESET);
        String input = ioHandler.handleInput();

        int[] date = DataConverter.getDateInArray(input);

        if (date != null) {
            storageQueryService.getHardToSell(date[2], date[1], date[0]).forEach(System.out::println);
        } else ioHandler.showMessage(Colors.YELLOW + "НЕВЕРНЫЙ ФОРМАТ ДАТЫ" + Colors.RESET);

    }

    public void getBookDescription() {
        ioHandler.showMessage(Colors.BLUE + "Введите название книги:" + Colors.RESET);

        try {
            ioHandler.showMessage(storageQueryService.getBookDescription(ioHandler.handleInput()));
        } catch (Exception e) { ioHandler.showMessage(Colors.YELLOW + "КНИГА НЕ НАЙДЕНА" + Colors.RESET); }
    }
}
