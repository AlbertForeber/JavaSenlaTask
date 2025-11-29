package container;

import task.model.entity.Request;
import task.repository.RequestManagerRepository;
import task.service.request.io.RequestImportConstants;
import task.service.request.io.RequestImportService;
import task.utils.FileParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class CsvRequestImportService implements RequestImportService {
    private final RequestManagerRepository requestManagerRepository;

    public CsvRequestImportService(RequestManagerRepository requestManagerRepository) {
        this.requestManagerRepository = requestManagerRepository;
    }

    @Override
    public void importRequest(String fileName) throws IllegalArgumentException, IOException {
        Map<String, ArrayList<String>> fields = FileParser.parseFile(
                fileName, RequestImportConstants.REQUIRED_FIELDS, RequestImportConstants.REQUIRED_FIELDS
        );

        for (int i = 0; i < fields.get("id").size(); i++) {

            int id = FileParser.parseNumericField(fields.get("id").get(i), "id");
            int amount = FileParser.parseNumericField(fields.get("amount").get(i), "amount");

            Request duplicate = requestManagerRepository.getRequest(id);

            if (duplicate != null && !Objects.equals(duplicate.getBookName(), fields.get("bookName").get(i)))
                requestManagerRepository.cancelRequests(duplicate.getBookName());

            requestManagerRepository.addRequest(
                    id,
                    fields.get("bookName").get(i)
            );

            Request newRequest = requestManagerRepository.getRequest(id);

            newRequest.setAmount(amount);
        }
    }
}
