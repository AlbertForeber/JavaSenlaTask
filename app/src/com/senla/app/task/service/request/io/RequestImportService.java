package com.senla.app.task.service.request.io;

import java.io.IOException;

public interface RequestImportService {
    void importRequest(String fileName) throws IllegalArgumentException, IOException;
}
