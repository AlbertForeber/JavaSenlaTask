package task.service;

import task.model.entity.Request;
import task.service.order.OrderQueryService;
import task.service.order.OrderService;
import task.service.request.RequestQueryService;
import task.service.storage.StorageQueryService;

import java.io.IOException;

public class StateLoadSaveFacade {
    private final OrderQueryService orderQueryService;
    private final RequestQueryService requestQueryService;
    private final StorageQueryService storageQueryService;

    public StateLoadSaveFacade(
            OrderQueryService orderQueryService,
            RequestQueryService requestQueryService,
            StorageQueryService storageQueryService
    ) {
        this.orderQueryService = orderQueryService;
        this.requestQueryService = requestQueryService;
        this.storageQueryService = storageQueryService;
    }

    public void loadState(String path) throws IOException, ClassNotFoundException, IllegalArgumentException {
        checkPath(path);

        storageQueryService.loadState(path);
        orderQueryService.loadState(path);
        requestQueryService.loadState(path);
    }

    public void saveState(String path) throws IOException, IllegalArgumentException {
        checkPath(path);

        storageQueryService.saveState(path);
        orderQueryService.saveState(path);
        requestQueryService.saveState(path);
    }

    private void checkPath(String path) throws IllegalArgumentException {
        if (!path.endsWith("\\") && !path.endsWith("/")) {
            throw new IllegalArgumentException("Неверно указан путь");
        }
    }
}
