package com.senla.app.service.order.io;

import java.io.IOException;

public interface OrderExportService {

    void exportOrder(int orderId, String path) throws IllegalArgumentException, IOException;
}
