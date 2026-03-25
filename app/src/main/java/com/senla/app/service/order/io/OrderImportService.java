package com.senla.app.service.order.io;

import java.io.IOException;

public interface OrderImportService {

    void importOrder(String fileName) throws IllegalArgumentException, IOException;
}
