package task.controller;

import task.model.entity.sortby.BookSortBy;
import task.service.storage.StorageQueryService;
import task.service.storage.StorageService;
import task.service.storage.io.StorageExportService;
import task.service.storage.io.StorageImportService;
import task.utils.Colors;
import task.utils.DataConverter;
import task.view.IOHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StorageController extends BaseController {
    private final StorageService storageService;
    private final StorageQueryService storageQueryService;
    private final StorageImportService storageImportService;
    private final StorageExportService storageExportService;


    public StorageController(
            StorageQueryService storageQueryService,
            StorageService storageService,
            StorageImportService storageImportService,
            StorageExportService storageExportService,
            IOHandler ioHandler
    ) {
        super(ioHandler);
        this.storageQueryService = storageQueryService;
        this.storageService = storageService;
        this.storageImportService = storageImportService;
        this.storageExportService = storageExportService;
    }

    public void writeOffBook() {
        ioHandler.showMessage(Colors.BLUE + "Введите ID книги:" + Colors.RESET);

        try {
            storageService.writeOffBook(Integer.parseInt(ioHandler.handleInput()));
        } catch (NumberFormatException e) {
            ioHandler.showMessage(Colors.YELLOW + "ID ДОЛЖЕН БЫТЬ ЧИСЛЕННЫМ ЗНАЧЕНИЕМ" + Colors.RESET);
        }
        catch (Exception e) {
            ioHandler.showMessage(Colors.YELLOW + "КНИГА НЕДОСТУПНА" + Colors.RESET);
        }

    }

    public void addBookToStorage() {
        ioHandler.showMessage(Colors.BLUE + "Введите ID книги:" + Colors.RESET);

        try {
            storageService.addBookToStorage(Integer.parseInt(ioHandler.handleInput()));
        } catch (NumberFormatException e) {
            ioHandler.showMessage(Colors.YELLOW + "ID ДОЛЖЕН БЫТЬ ЧИСЛЕННЫМ ЗНАЧЕНИЕМ" + Colors.RESET);
        }
        catch (Exception e) {
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
            storageQueryService.getBookDescription(Integer.parseInt(ioHandler.handleInput()));
        } catch (NumberFormatException e) {
            ioHandler.showMessage(Colors.YELLOW + "ID ДОЛЖЕН БЫТЬ ЧИСЛЕННЫМ ЗНАЧЕНИЕМ" + Colors.RESET);
        }
        catch (Exception e) {
            ioHandler.showMessage(Colors.YELLOW + "КНИГА НЕДОСТУПНА" + Colors.RESET);
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
