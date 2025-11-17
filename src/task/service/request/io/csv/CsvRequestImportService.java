package task.service.request.io.csv;

import task.model.entity.Request;
import task.repository.RequestManagerRepository;
import task.service.request.io.RequestImportService;
import task.service.request.io.RequestImportConstants;
import task.utils.FileParser;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class CsvRequestImportService implements RequestImportService {
    private final RequestManagerRepository requestManagerRepository;

    public CsvRequestImportService(RequestManagerRepository requestManagerRepository) {
        this.requestManagerRepository = requestManagerRepository;
    }

    @Override
    public void importRequest(String fileName) throws IllegalArgumentException, IOException {
        Map<String, String> fields = FileParser.parseFile(fileName, RequestImportConstants.REQUIRED_FIELDS);
        FileParser.validateRequiredFields(fields, RequestImportConstants.REQUIRED_FIELDS);

        int id = FileParser.parseNumericField(fields.get("id"), "id");
        int amount = FileParser.parseNumericField(fields.get("amount"), "amount");

        Request duplicate = requestManagerRepository.getRequest(id);

        if (duplicate != null  && !Objects.equals(duplicate.getBookName(), fields.get("bookName")))
            throw new IllegalArgumentException("Запрос с таким ID уже существует и названия книг не совпадают");

        requestManagerRepository.addRequest(
                id,
                fields.get("bookName")
        );

        Request newRequest = requestManagerRepository.getRequest(id);

        newRequest.setAmount(newRequest.getAmount() + amount - 1);
    }
}
