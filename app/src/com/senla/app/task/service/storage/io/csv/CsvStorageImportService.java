package com.senla.app.task.service.storage.io.csv;

import com.senla.annotation.InjectTo;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.status.BookStatus;
import com.senla.app.task.repository.StorageRepository;
import com.senla.app.task.repository.inmemory.InMemoryStorageRepository;
import com.senla.app.task.service.storage.io.StorageImportService;
import com.senla.app.task.service.storage.io.BookImportConstants;
import com.senla.app.task.utils.DataConverter;
import com.senla.app.task.utils.FileParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class CsvStorageImportService implements StorageImportService {

    @InjectTo(useImplementation = InMemoryStorageRepository.class)
    private StorageRepository storageRepository;

    public CsvStorageImportService() {}

    @Override
    public void importBook(String fileName) throws IllegalArgumentException, IOException {
        Map<String, ArrayList<String>> fields = FileParser.parseFile(
                fileName, BookImportConstants.ALLOWED_FIELDS, BookImportConstants.REQUIRED_FIELDS
        );

        for (int i = 0; i < fields.get("id").size(); i ++) {

            int[] publicationDate = DataConverter.getDateInArray(fields.get("publicationDate").get(i));
            int[] admissionDate = DataConverter.getDateInArray(fields.get("admissionDate").get(i));

            if (publicationDate == null) throw new IllegalArgumentException("Неверный формат даты публикации");
            if (admissionDate == null) throw new IllegalArgumentException("Неверный формат даты поступления");


            BookStatus bookStatus = getVerifiedStatus(fields.get("status").get(i), fields, i);

            Book book = new Book(
                    FileParser.parseNumericField(fields.get("id").get(i), "id"),
                    fields.get("title").get(i),
                    fields.get("description").get(i),
                    admissionDate[2],
                    admissionDate[1],
                    admissionDate[0],
                    bookStatus
            );

            book.setPublicationDate(publicationDate[2], publicationDate[1], publicationDate[0]);

            if (fields.get("reservist") != null && !fields.get("reservist").get(i).trim().isEmpty()) book.setStatus(BookStatus.RESERVED, fields.get("reservist").get(i));

            storageRepository.addBook(book);
        }
    }

    public BookStatus getVerifiedStatus(String status, Map<String, ArrayList<String>> fields, int index) throws IllegalArgumentException {
        return switch (status) {
            case "FREE" -> {
                if (fields.get("reservist") != null &&
                        !fields.get("reservist").get(index).trim().isEmpty())
                    throw new IllegalArgumentException("Книга со статусом FREE не может иметь резервиста");
                yield BookStatus.FREE;
            }

            case "SOLD_OUT" -> {
                if (fields.get("reservist") != null &&
                        !fields.get("reservist").get(index).trim().isEmpty())
                    throw new IllegalArgumentException("Книга со статусом SOLD_OUT не может иметь резервиста");

                yield BookStatus.SOLD_OUT;
            }

            case "RESERVED" -> {
                if (fields.get("reservist") == null ||
                        fields.get("reservist").get(index).trim().isEmpty())
                    throw new IllegalArgumentException("Книга со статусом RESERVED должна иметь резервиста");

                yield BookStatus.RESERVED;
            }
            default -> throw new IllegalArgumentException("Неверный статус: " + fields.get("status").get(index));
        };
    }

}
