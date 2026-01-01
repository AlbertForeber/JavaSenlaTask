package com.senla.app.task.service.request.io.csv;

import com.senla.annotation.InjectTo;
import com.senla.app.task.model.entity.Request;
import com.senla.app.task.repository.RequestManagerRepository;
import com.senla.app.task.repository.db.DbRequestManagerRepository;
import com.senla.app.task.service.request.io.RequestImportService;
import com.senla.app.task.service.request.io.RequestImportConstants;
import com.senla.app.task.utils.FileParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class CsvRequestImportService implements RequestImportService {

    @InjectTo(useImplementation = DbRequestManagerRepository.class)
    private RequestManagerRepository requestManagerRepository;

    public CsvRequestImportService() {}

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
