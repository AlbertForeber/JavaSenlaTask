import task.controller.MainController;
import task.controller.OrderController;
import task.controller.RequestController;
import task.controller.StorageController;
import task.repository.OrderManagerRepository;
import task.repository.RequestManagerRepository;
import task.repository.StorageRepository;
import task.repository.inmemory.InMemoryOrderManagerRepository;
import task.repository.inmemory.InMemoryRequestManagerRepository;
import task.repository.inmemory.InMemoryStorageRepository;
import task.service.StateLoadSaveFacade;
import task.service.order.OrderQueryService;
import task.service.order.OrderService;
import task.service.order.io.OrderExportService;
import task.service.order.io.OrderImportService;
import task.service.order.io.csv.CsvOrderExportService;
import task.service.order.io.csv.CsvOrderImportService;
import task.service.request.RequestQueryService;
import task.service.request.RequestService;
import task.service.request.io.RequestExportService;
import task.service.request.io.RequestImportService;
import task.service.request.io.csv.CsvRequestExportService;
import task.service.request.io.csv.CsvRequestImportService;
import task.service.storage.StorageQueryService;
import task.service.storage.StorageService;
import task.service.storage.io.StorageExportService;
import task.service.storage.io.StorageImportService;
import task.service.storage.io.csv.CsvStorageExportService;
import task.service.storage.io.csv.CsvStorageImportService;
import task.utils.PropertyConverter;
import task.view.*;
import task.view.console.*;
import task.view.enums.ControllerKey;
import task.view.enums.NavigateTo;
import java.io.File;


public class Application {
    private final static String DELIMITER = File.separator;
    private final static String PATH_TO_CONFIG = "." + DELIMITER + "config" + DELIMITER + "config.properties";
    private final static String PATH_TO_STATE = "." + DELIMITER + "state" + DELIMITER;

    public static void main(String[] args) {
        // IOHandler
        IOHandler ioHandler = new ConsoleIOHandler();

        // MainController - preparation
        MainController mainController = new MainController(ioHandler, PATH_TO_CONFIG, PATH_TO_STATE);

        // Repositories
        StorageRepository storageRepository = new InMemoryStorageRepository();
        OrderManagerRepository orderManagerRepository = new InMemoryOrderManagerRepository();
        RequestManagerRepository requestManagerRepository = new InMemoryRequestManagerRepository();


        // Services
        StorageService storageService = new StorageService(
                storageRepository,
                requestManagerRepository,
                PropertyConverter.getBoolean(mainController.getProperties(), "cancelRequests", true)
        );

        StorageQueryService storageQueryService = new StorageQueryService(
                storageRepository,
                PropertyConverter.getInt(mainController.getProperties(), "liquidMonths", 6)
        );

        OrderService orderService = new OrderService(orderManagerRepository, storageRepository, requestManagerRepository);
        OrderQueryService orderQueryService = new OrderQueryService(orderManagerRepository);

        RequestService requestService = new RequestService(requestManagerRepository, storageRepository);
        RequestQueryService requestQueryService = new RequestQueryService(requestManagerRepository);

        // Facade
        StateLoadSaveFacade stateLoadSaveFacade = new StateLoadSaveFacade(orderQueryService, requestQueryService, storageQueryService);

        // Menu
        MenuBuilder menuBuilder = new ConsoleMenuBuilder();
        MenuRenderer menuRenderer = new ConsoleMenuRenderer();

        // Controller Registry
        ControllerRegistry controllerRegistry = new ConsoleControllerRegistry();

        // Navigator
        Navigator navigator = new ConsoleNavigator(menuBuilder, menuRenderer, ioHandler);

        // Menu Builder Setup
        menuBuilder.setNavigator(navigator);
        menuBuilder.setControllerRegistry(controllerRegistry);

        // Import/Export
        OrderImportService orderImportService = new CsvOrderImportService(orderManagerRepository, storageRepository, orderService);
        OrderExportService orderExportService = new CsvOrderExportService(orderManagerRepository);

        RequestImportService requestImportService = new CsvRequestImportService(requestManagerRepository);
        RequestExportService requestExportService = new CsvRequestExportService(requestManagerRepository);

        StorageImportService storageImportService = new CsvStorageImportService(storageRepository);
        StorageExportService storageExportService = new CsvStorageExportService(storageRepository);

        // Controllers
        StorageController storageController = new StorageController(
                storageQueryService, storageService, storageImportService, storageExportService, ioHandler
        );

        OrderController orderController = new OrderController(
                orderQueryService, orderService, orderImportService, orderExportService, ioHandler
        );

        RequestController requestController = new RequestController(
                requestQueryService, requestService, requestImportService, requestExportService, ioHandler
        );

        mainController.setStateLoadSaveFacade(stateLoadSaveFacade);

        controllerRegistry.registerController(ControllerKey.STORAGE, storageController);
        controllerRegistry.registerController(ControllerKey.ORDER, orderController);
        controllerRegistry.registerController(ControllerKey.REQUEST, requestController);
        controllerRegistry.registerController(ControllerKey.MAIN, mainController);


        // START
        mainController.loadState();
        navigator.navigateTo(NavigateTo.MAIN);
    }
}
