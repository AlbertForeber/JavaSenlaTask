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
            bw.write("id;" + book.getId());
            bw.newLine();

            bw.write("title;" + book.getTitle());
            bw.newLine();

            bw.write("description;" + book.getDescription());
            bw.newLine();

            bw.write("publicationDate;" + DataConverter.calendarToString(book.getPublicationDate()));
            bw.newLine();

            bw.write("admissionDate;" + DataConverter.calendarToString(book.getAdmissionDate()));
            bw.newLine();

            bw.write("price;" + book.getPrice());
            bw.newLine();

            bw.write("status;" + book.getStatus().name());
            bw.newLine();

            String reservist = book.getReservist();
            if (reservist != null) {
                bw.write("reservist;" + reservist);
                bw.newLine();
            }
        }
    }
}
