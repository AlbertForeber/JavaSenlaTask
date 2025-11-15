package task.service.storage.io;

import java.io.IOException;

public interface BookExport {
    void exportBook(int bookId) throws IllegalArgumentException, IOException;
}
