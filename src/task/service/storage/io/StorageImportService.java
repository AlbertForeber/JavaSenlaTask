package task.service.storage.io;

import java.io.IOException;

public interface StorageImportService {
    void importBook(String fileName) throws IllegalArgumentException, IOException;
}
