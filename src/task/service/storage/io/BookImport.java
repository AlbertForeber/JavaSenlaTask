package task.service.storage.io;

import java.io.IOException;

public interface BookImport {
    void importBook(String fileName) throws IllegalArgumentException, IOException;
}
