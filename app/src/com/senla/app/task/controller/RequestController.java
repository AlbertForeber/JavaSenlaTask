package com.senla.app.task.controller;

import com.senla.annotation.InjectTo;
import com.senla.app.task.model.entity.sortby.RequestSortBy;
import com.senla.app.task.service.request.RequestQueryService;
import com.senla.app.task.service.request.RequestService;
import com.senla.app.task.service.request.io.RequestExportService;
import com.senla.app.task.service.request.io.RequestImportService;
import com.senla.app.task.utils.Colors;
import com.senla.app.task.view.IOHandler;

import java.io.FileNotFoundException;
import java.io.IOException;

public class RequestController extends BaseController {
    @InjectTo
    private RequestService requestService;

    @InjectTo
    private RequestQueryService requestQueryService;

    @InjectTo
    private RequestImportService requestImportService;

    @InjectTo
    private RequestExportService requestExportService;

    @InjectTo
    private IOHandler ioHandler;

    public RequestController() {
        super();
    }

    public void createRequest() {
        ioHandler.showMessage(Colors.BLUE + "Введите ID книги на которую создается запрос:" + Colors.RESET);

        try {
            requestService.createRequest(Integer.parseInt(ioHandler.handleInput()));
        } catch (NumberFormatException e) {
            ioHandler.showMessage(Colors.YELLOW + "ID ДОЛЖЕН БЫТЬ ЧИСЛЕННЫМ ЗНАЧЕНИЕМ" + Colors.RESET);
        }
        catch (Exception e) {
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

        requestQueryService.getSorted(requestSortBy).stream()
                .map(Object::toString)
                .forEach(System.out::println);
    }

    public void importRequest() {
        ioHandler.showMessage(Colors.BLUE + "Введите путь к файлу" + Colors.RESET);
        String fileName = ioHandler.handleInput();

        try {
            requestImportService.importRequest(fileName);
            ioHandler.showMessage(Colors.YELLOW + "Запрос '" + fileName + "' успешно импортирован" + Colors.RESET);
        } catch (FileNotFoundException e) {
            ioHandler.showMessage(Colors.YELLOW + "ФАЙЛ '" + fileName + "' НЕ НАЙДЕН" + Colors.RESET);
        } catch (IOException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ВО ВРЕМЯ ЧТЕНИЯ ДОКУМЕНТА" + Colors.RESET);
        } catch (IllegalArgumentException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА: " + e.getMessage() + Colors.RESET);
        }
    }

    public void exportRequest() {
        ioHandler.showMessage(Colors.BLUE + "Введите ID запроса" + Colors.RESET);

        try {
            int requestId = Integer.parseInt(ioHandler.handleInput());

            ioHandler.showMessage(Colors.BLUE + "Введите путь для файла (Без имени файла, с разделителем на конце)" + Colors.RESET);
            ioHandler.showMessage(Colors.BLUE + "Пример: ./dir1/dir2/dir3/ (для Linux)" + Colors.RESET);

            requestExportService.exportRequest(requestId, ioHandler.handleInput());
            ioHandler.showMessage(Colors.YELLOW + "Запрос с ID: '" + requestId + "' успешно экспортирован" + Colors.RESET);

        } catch (NumberFormatException e) {
            ioHandler.showMessage(Colors.YELLOW + "ID ДОЛЖЕН БЫТЬ ЧИСЛЕННЫМ ЗНАЧЕНИЕМ" + Colors.RESET);
        } catch (IOException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА ВО ВРЕМЯ СОЗДАНИЯ ДОКУМЕНТА" + Colors.RESET);
        } catch (IllegalArgumentException e) {
            ioHandler.showMessage(Colors.YELLOW + "ОШИБКА: " + e.getMessage() + Colors.RESET);
        }
    }

}
