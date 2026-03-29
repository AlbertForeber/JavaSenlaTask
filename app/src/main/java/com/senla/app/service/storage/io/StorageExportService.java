package com.senla.app.service.storage.io;

import java.io.IOException;

public interface StorageExportService {

    void exportBook(int bookId, String path) throws IllegalArgumentException, IOException;
}
