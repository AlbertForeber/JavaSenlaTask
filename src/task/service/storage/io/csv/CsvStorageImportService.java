package task.service.storage.io.csv;

import task.model.entity.Book;
import task.model.entity.status.BookStatus;
import task.repository.StorageRepository;
import task.service.storage.io.StorageImportService;
import task.service.storage.io.BookImportConstants;
import task.utils.DataConverter;
import task.utils.FileParser;

import java.io.IOException;
import java.util.Map;

public class CsvStorageImportService implements StorageImportService {
    private final StorageRepository storageRepository;

    public CsvStorageImportService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    @Override
    public void importBook(String fileName) throws IllegalArgumentException, IOException {
        Map<String, String> fields = FileParser.parseFile(fileName, BookImportConstants.ALLOWED_FIELDS);
        FileParser.validateRequiredFields(fields, BookImportConstants.REQUIRED_FIELDS);

        int[] publicationDate = DataConverter.getDateInArray(fields.get("publicationDate"));
        int[] admissionDate = DataConverter.getDateInArray(fields.get("admissionDate"));

        if (publicationDate == null) throw new IllegalArgumentException("Неверный формат даты публикации");
        if (admissionDate == null) throw new IllegalArgumentException("Неверный формат даты поступления");


        BookStatus bookStatus = getVerifiedStatus(fields);

        Book book = new Book(
            FileParser.parseNumericField(fields.get("id"), "id"),
                fields.get("title"),
                fields.get("description"),
                admissionDate[2],
                admissionDate[1],
                admissionDate[0],
                bookStatus
        );

        book.setPublicationDate(publicationDate[2], publicationDate[1], publicationDate[0]);

        if (fields.get("reservist") != null) book.setStatus(BookStatus.RESERVED, fields.get("reservist"));

        storageRepository.addBook(book);
    }

    public BookStatus getVerifiedStatus(Map<String, String> fields) throws IllegalArgumentException {
        return switch (fields.get("status")) {
            case "FREE" -> {
                if (fields.get("reservist") != null)
                    throw new IllegalArgumentException("Книга со статусом FREE не может иметь резервиста");
                yield BookStatus.FREE;
            }

            case "SOLD_OUT" -> {
                if (fields.get("reservist") != null)
                    throw new IllegalArgumentException("Книга со статусом SOLD_OUT не может иметь резервиста");

                yield BookStatus.SOLD_OUT;
            }

            case "RESERVED" -> {
                if (fields.get("reservist") == null)
                    throw new IllegalArgumentException("Книга со статусом RESERVED должна иметь резервиста");

                yield BookStatus.RESERVED;
            }
            default -> throw new IllegalArgumentException("Неверный статус: " + fields.get("status"));
        };
    }

}
