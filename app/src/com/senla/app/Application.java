package com.senla.app;

import com.senla.annotation_processor.ConfigProcessor;
import com.senla.annotation_processor.InjectProcessor;
import com.senla.app.task.controller.MainController;
import com.senla.app.task.controller.OrderController;
import com.senla.app.task.controller.RequestController;
import com.senla.app.task.controller.StorageController;
import com.senla.app.task.repository.OrderManagerRepository;
import com.senla.app.task.repository.RequestManagerRepository;
import com.senla.app.task.repository.StorageRepository;
import com.senla.app.task.repository.inmemory.InMemoryOrderManagerRepository;
import com.senla.app.task.repository.inmemory.InMemoryRequestManagerRepository;
import com.senla.app.task.repository.inmemory.InMemoryStorageRepository;
import com.senla.app.task.service.StateLoadSaveFacade;
import com.senla.app.task.service.order.OrderQueryService;
import com.senla.app.task.service.order.OrderService;
import com.senla.app.task.service.order.io.OrderExportService;
import com.senla.app.task.service.order.io.OrderImportService;
import com.senla.app.task.service.order.io.csv.CsvOrderExportService;
import com.senla.app.task.service.order.io.csv.CsvOrderImportService;
import com.senla.app.task.service.request.RequestQueryService;
import com.senla.app.task.service.request.RequestService;
import com.senla.app.task.service.request.io.RequestExportService;
import com.senla.app.task.service.request.io.RequestImportService;
import com.senla.app.task.service.request.io.csv.CsvRequestExportService;
import com.senla.app.task.service.request.io.csv.CsvRequestImportService;
import com.senla.app.task.service.storage.StorageQueryService;
import com.senla.app.task.service.storage.StorageService;
import com.senla.app.task.service.storage.io.StorageExportService;
import com.senla.app.task.service.storage.io.StorageImportService;
import com.senla.app.task.service.storage.io.csv.CsvStorageExportService;
import com.senla.app.task.service.storage.io.csv.CsvStorageImportService;
import com.senla.app.task.view.*;
import com.senla.app.task.view.console.*;
import com.senla.app.task.view.enums.ControllerKey;
import com.senla.app.task.view.enums.NavigateTo;
import java.io.File;


public class Application {
    private final static String DELIMITER = File.separator;
    private final static String PATH_TO_STATE = "." + DELIMITER + "state" + DELIMITER;

    public static void main(String[] args) {
        // IOHandler
        IOHandler ioHandler = new ConsoleIOHandler();
        InjectProcessor.addDependency(IOHandler.class, ioHandler);


        // MainController - preparation
        MainController mainController = new MainController(ioHandler, PATH_TO_STATE);
        InjectProcessor.addDependency(MainController.class, mainController);


        // Repositories
        StorageRepository storageRepository = new InMemoryStorageRepository();
        InjectProcessor.addDependency(StorageRepository.class, storageRepository);

        OrderManagerRepository orderManagerRepository = new InMemoryOrderManagerRepository();
        InjectProcessor.addDependency(OrderManagerRepository.class, orderManagerRepository);

        RequestManagerRepository requestManagerRepository = new InMemoryRequestManagerRepository();
        InjectProcessor.addDependency(RequestManagerRepository.class, requestManagerRepository);


        // Services
        StorageService storageService = mainController.addToNeedDi(new StorageService());
        InjectProcessor.addDependency(StorageService.class, storageService);
        mainController.addToConfigurable(storageService);

        StorageQueryService storageQueryService = mainController.addToNeedDi(new StorageQueryService());
        InjectProcessor.addDependency(StorageQueryService.class, storageQueryService);
        mainController.addToConfigurable(storageQueryService);

        OrderService orderService = mainController.addToNeedDi(new OrderService());
        InjectProcessor.addDependency(OrderService.class, orderService);

        OrderQueryService orderQueryService = mainController.addToNeedDi(new OrderQueryService());
        InjectProcessor.addDependency(OrderQueryService.class, orderQueryService);

        RequestService requestService = mainController.addToNeedDi(new RequestService());
        InjectProcessor.addDependency(RequestService.class, requestService);

        RequestQueryService requestQueryService = new RequestQueryService();
        InjectProcessor.addDependency(RequestQueryService.class, requestQueryService);

        // Facade
        StateLoadSaveFacade stateLoadSaveFacade = mainController.addToNeedDi(new StateLoadSaveFacade());
        InjectProcessor.addDependency(StateLoadSaveFacade.class ,stateLoadSaveFacade);

        // Menu
        MenuBuilder menuBuilder = mainController.addToNeedDi(new ConsoleMenuBuilder());
        InjectProcessor.addDependency(MenuBuilder.class, menuBuilder);

        MenuRenderer menuRenderer = new ConsoleMenuRenderer();
        InjectProcessor.addDependency(MenuRenderer.class, menuRenderer);




        // Navigator
        Navigator navigator = mainController.addToNeedDi(new ConsoleNavigator());
        InjectProcessor.addDependency(Navigator.class, navigator);


        // Import/Export
        OrderImportService orderImportService = mainController.addToNeedDi(new CsvOrderImportService());
        InjectProcessor.addDependency(OrderImportService.class, orderImportService);

        OrderExportService orderExportService = mainController.addToNeedDi(new CsvOrderExportService());
        InjectProcessor.addDependency(OrderExportService.class, orderExportService);

        RequestImportService requestImportService = mainController.addToNeedDi(new CsvRequestImportService());
        InjectProcessor.addDependency(RequestImportService.class, requestImportService);

        RequestExportService requestExportService = mainController.addToNeedDi(new CsvRequestExportService());
        InjectProcessor.addDependency(RequestExportService.class, requestExportService);

        StorageImportService storageImportService = mainController.addToNeedDi(new CsvStorageImportService());
        InjectProcessor.addDependency(StorageImportService.class, storageImportService);

        StorageExportService storageExportService = mainController.addToNeedDi(new CsvStorageExportService());
        InjectProcessor.addDependency(StorageExportService.class, storageExportService);


        // Controllers
        StorageController storageController = mainController.addToNeedDi(new StorageController());
        InjectProcessor.addDependency(StorageController.class, storageController);

        OrderController orderController = mainController.addToNeedDi(new OrderController());
        InjectProcessor.addDependency(OrderController.class, orderController);

        RequestController requestController = mainController.addToNeedDi(new RequestController());
        InjectProcessor.addDependency(RequestController.class, requestController);


        mainController.setStateLoadSaveFacade(stateLoadSaveFacade);


        // START
        mainController.injectDependencies();
        mainController.applyConfig();
        mainController.loadState();
        navigator.navigateTo(NavigateTo.MAIN);
    }
}
