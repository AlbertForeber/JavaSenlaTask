package container;

import task.repository.OrderManagerRepository;
import task.repository.RequestManagerRepository;
import task.repository.StorageRepository;
import task.repository.inmemory.InMemoryOrderManagerRepository;
import task.repository.inmemory.InMemoryRequestManagerRepository;
import task.repository.inmemory.InMemoryStorageRepository;
import task.controller.StorageController;
import task.service.order.OrderService;
import task.service.order.io.OrderExportService;
import task.service.order.io.OrderImportService;
import task.service.order.io.csv.CsvOrderExportService;
import task.service.order.io.csv.CsvOrderImportService;
import task.service.storage.StorageQueryService;
import task.service.storage.StorageService;
import task.service.storage.io.StorageExportService;
import task.service.storage.io.StorageImportService;
import task.service.storage.io.csv.CsvStorageExportService;
import task.service.storage.io.csv.CsvStorageImportService;
import task.view.*;
import task.view.console.*;
import task.view.enums.ControllerKey;
import task.view.enums.NavigateTo;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

public class Container {
    public static void main(String[] args) throws IOException {
        OrderManagerRepository orderManagerRepository = new InMemoryOrderManagerRepository();
        StorageRepository storageRepository = new InMemoryStorageRepository();
        RequestManagerRepository requestManagerRepository = new InMemoryRequestManagerRepository();

        OrderService orderService = new OrderService(orderManagerRepository, storageRepository, requestManagerRepository);

        OrderImportService orderImportService = new CsvOrderImportService(orderManagerRepository, storageRepository, orderService);
        OrderExportService orderExportService = new CsvOrderExportService(orderManagerRepository);

//        orderImportService.importOrder("/Users/albert/IdeaProjects/JavaSenlaTask/src/container/Untitled 1.csv");
//
//
//        orderExportService.exportOrder(1, "/Users/albert/IdeaProjects/JavaSenlaTask/src/container/");\

        StorageExportService storageExportService = new CsvStorageExportService(storageRepository);
//        storageExportService.exportBook(7, "/Users/albert/IdeaProjects/JavaSenlaTask/src/container/");

//        StorageImportService storageImportService = new CsvStorageImportService(storageRepository);
//        storageImportService.importBook("/Users/albert/IdeaProjects/JavaSenlaTask/src/container/book_7.csv");
//        System.out.println(storageRepository.getBook(11));
        System.out.println(Arrays.toString("asdasd;dsfsdf; ".split(";")));
    }
}
