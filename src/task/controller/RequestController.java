package task.controller;

import task.model.entity.sortby.OrderSortBy;
import task.model.entity.sortby.RequestSortBy;
import task.service.request.RequestQueryService;
import task.service.request.RequestService;
import task.utils.Colors;
import task.view.IOHandler;

public class RequestController extends BaseController {
    RequestService requestService;
    RequestQueryService requestQueryService;

    public RequestController(
            RequestQueryService requestQueryService,
            RequestService requestService,
            IOHandler ioHandler
    ) {
        super(ioHandler);
        this.requestQueryService = requestQueryService;
        this.requestService = requestService;
    }

    public void createRequest() {
        ioHandler.showMessage(Colors.BLUE + "Введите название книги:" + Colors.RESET);
        requestService.createRequest(ioHandler.handleInput());
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

}
