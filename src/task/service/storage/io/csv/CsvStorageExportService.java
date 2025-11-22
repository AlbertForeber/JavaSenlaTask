package task.service.storage.io.csv;

import task.model.entity.Book;
import task.repository.StorageRepository;
import task.service.storage.io.StorageExportService;
import task.utils.DataConverter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvStorageExportService implements StorageExportService {
    private final StorageRepository storageRepository;

    public CsvStorageExportService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    @Override
    public void exportBook(int bookId, String path) throws IllegalArgumentException, IOException {
        Book book = storageRepository.getBook(bookId);

        if (!path.endsWith("\\") && !path.endsWith("/")) {
            throw new IllegalArgumentException("Неверно указан путь");
        }

        if (book == null) throw new IllegalArgumentException("Книга с ID = " + bookId + "не найдена");

        writeBook(path + "book_" + bookId + ".csv", book);
    }

    private void writeBook(String fileName, Book book) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write("id;title;description;publicationDate;admissionDate;price;status;reservist");
            bw.newLine();

            bw.write(
                    book.getId() + ";" +
                            book.getTitle() + ";" +
                            book.getDescription() + ";" +
                            DataConverter.calendarToString(book.getPublicationDate()) + ";" +
                            DataConverter.calendarToString(book.getAdmissionDate()) + ";" +
                            book.getPrice() + ";" +
                            book.getStatus().name() + ";"
            );

            String reservist = book.getReservist();
            bw.write(reservist != null ? (reservist) : "");
        }
    }
}
