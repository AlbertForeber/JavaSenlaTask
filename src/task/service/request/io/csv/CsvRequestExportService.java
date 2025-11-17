package task.service.request.io.csv;

import task.model.entity.Request;
import task.repository.RequestManagerRepository;
import task.service.request.io.RequestExportService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvRequestExportService implements RequestExportService {

    private final RequestManagerRepository requestManagerRepository;

    public CsvRequestExportService(RequestManagerRepository requestManagerRepository) {
        this.requestManagerRepository = requestManagerRepository;
    }

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
            bw.write("id;" + request.getId());
            bw.newLine();

            bw.write("bookName;" + request.getBookName());
            bw.newLine();

            bw.write("amount;" + request.getAmount());
            bw.newLine();
        }
    }
}
