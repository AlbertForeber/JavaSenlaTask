package com.senla.app.task.service;

import com.senla.annotation.InjectTo;
import com.senla.app.task.service.order.OrderQueryService;
import com.senla.app.task.service.request.RequestQueryService;
import com.senla.app.task.service.storage.StorageQueryService;

import java.io.IOException;

public class StateLoadSaveFacade {

    @InjectTo
    private OrderQueryService orderQueryService;

    @InjectTo
    private RequestQueryService requestQueryService;

    @InjectTo(configurable = true)
    private StorageQueryService storageQueryService;

    public StateLoadSaveFacade() { }

    public void loadState(String path) throws IOException, ClassNotFoundException, IllegalArgumentException {
        checkPath(path);
    }

    public void saveState(String path) throws IOException, IllegalArgumentException {
        checkPath(path);
    }

    private void checkPath(String path) throws IllegalArgumentException {
        if (!path.endsWith("\\") && !path.endsWith("/")) {
            throw new IllegalArgumentException("Неверно указан путь");
        }
    }
}
