package task.service.order.io;

import java.io.IOException;

public interface OrderImport {
    void importOrder(String fileName) throws IllegalArgumentException, IOException;
}
