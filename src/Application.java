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
import task.service.request.RequestQueryService;
import task.service.request.RequestService;
import task.service.storage.StorageQueryService;
import task.service.storage.StorageService;
import task.view.*;
import task.view.console.*;
import task.view.enums.ControllerKey;
import task.view.enums.NavigateTo;

public class Application {
    public static void main(String[] args) {
        // Repositories
        StorageRepository storageRepository = new InMemoryStorageRepository();
        OrderManagerRepository orderManagerRepository = new InMemoryOrderManagerRepository();
        RequestManagerRepository requestManagerRepository = new InMemoryRequestManagerRepository();


        // Services
        StorageService storageService = new StorageService(storageRepository, requestManagerRepository);
        StorageQueryService storageQueryService = new StorageQueryService(storageRepository);

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


        // Controllers
        StorageController storageController = new StorageController(storageQueryService, storageService, ioHandler);
        OrderController orderController = new OrderController(orderQueryService, orderService, ioHandler);
        RequestController requestController = new RequestController(requestQueryService, requestService, ioHandler);

        controllerRegistry.registerController(ControllerKey.STORAGE, storageController);
        controllerRegistry.registerController(ControllerKey.ORDER, orderController);
        controllerRegistry.registerController(ControllerKey.REQUEST, requestController);


        // START
        navigator.navigateTo(NavigateTo.MAIN);
    }
}
