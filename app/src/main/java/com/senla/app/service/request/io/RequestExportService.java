package com.senla.app.service.request.io;

import java.io.IOException;

public interface RequestExportService {

    void exportRequest(int requestId, String path) throws IllegalArgumentException, IOException;
}
