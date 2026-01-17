package com.senla.app.task.service.request.io.csv;

import com.senla.annotation.InjectTo;
import com.senla.app.task.model.entity.Request;
import com.senla.app.task.repository.RequestManagerRepository;
import com.senla.app.task.repository.db.DbRequestManagerRepository;
import com.senla.app.task.service.request.io.RequestExportService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvRequestExportService implements RequestExportService {

    @InjectTo(useImplementation = DbRequestManagerRepository.class)
    private RequestManagerRepository requestManagerRepository;

    public CsvRequestExportService() {}

    @Override
    public void exportRequest(int requestId, String path) throws IllegalArgumentException, IOException {
        Request request = requestManagerRepository.getRequest(requestId);

        if (!path.endsWith("\\") && !path.endsWith("/")) {
            throw new IllegalArgumentException("Неверно указан путь");
        }

        if (request == null) throw new IllegalArgumentException("Запрос с ID = " + requestId + "не найден");

        writeRequest(path + "request_" + requestId + ".csv", request);
    }

    private void writeRequest(String fileName, Request request) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write("id;bookName;amount");
            bw.newLine();

            bw.write(
                    request.getId() + ";" +
                            request.getBookName() + ";" +
                            request.getAmount()
            );
        }
    }
}
