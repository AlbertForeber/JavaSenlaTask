package task.service.order.io;

import java.io.IOException;

public interface OrderExport {
    void exportOrder(int orderId) throws IllegalArgumentException, IOException;
}
