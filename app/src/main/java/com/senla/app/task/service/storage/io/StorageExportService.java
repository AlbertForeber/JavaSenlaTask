package com.senla.app.task.service.storage.io;

import java.io.IOException;

public interface StorageExportService {
    void exportBook(int bookId, String path) throws IllegalArgumentException, IOException;
}
