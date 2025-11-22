import task.controller.OrderController;
import task.controller.RequestController;
import task.controller.StorageController;
import task.repository.OrderManagerRepository;
import task.repository.RequestManagerRepository;
import task.repository.StorageRepository;
import task.repository.inmemory.InMemoryOrderManagerRepository;
import task.repository.inmemory.InMemoryRequestManagerRepository;
import task.repository.inmemory.InMemoryStorageRepository;
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
import task.utils.PropertyLoader;
import task.view.*;
import task.view.console.*;
import task.view.enums.ControllerKey;
import task.view.enums.NavigateTo;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Application {
    private final static String PATH_TO_CONFIG = "C:\\Users\\Administrator\\Desktop\\JavaSenlaTask\\src\\task\\config";

    public static void main(String[] args) {
        // Config
        Properties properties;
        try {
            properties = PropertyLoader.loadProperties(PATH_TO_CONFIG);
        } catch (IOException e) {
            System.err.println(e);
            properties = new Properties();
        }

        // Repositories
        StorageRepository storageRepository = new InMemoryStorageRepository();
        OrderManagerRepository orderManagerRepository = new InMemoryOrderManagerRepository();
        RequestManagerRepository requestManagerRepository = new InMemoryRequestManagerRepository();


        // Services
        StorageService storageService = new StorageService(
                storageRepository,
                requestManagerRepository,
                PropertyConverter.getBoolean(properties, "cancelRequests", true)
        );

        StorageQueryService storageQueryService = new StorageQueryService(
                storageRepository,
                PropertyConverter.getInt(properties, "liquidMonths", 6)
        );

        OrderService orderService = new OrderService(orderManagerRepository, storageRepository, requestManagerRepository);
        OrderQueryService orderQueryService = new OrderQueryService(orderManagerRepository);

        RequestService requestService = new RequestService(requestManagerRepository);
        RequestQueryService requestQueryService = new RequestQueryService(requestManagerRepository);


        // Menu
        MenuBuilder menuBuilder = new ConsoleMenuBuilder();
        MenuRenderer menuRenderer = new ConsoleMenuRenderer();

        // IOHandler
        IOHandler ioHandler = new ConsoleIOHandler();

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

        controllerRegistry.registerController(ControllerKey.STORAGE, storageController);
        controllerRegistry.registerController(ControllerKey.ORDER, orderController);
        controllerRegistry.registerController(ControllerKey.REQUEST, requestController);


        // START
        navigator.navigateTo(NavigateTo.MAIN);
    }
}
