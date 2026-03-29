package com.senla.app.controller;

import com.senla.annotation.LoggingOperation;
import com.senla.app.model.dto.response.BookResponse;
import com.senla.app.model.dto.response.CollectionResponse;
import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.sortby.BookSortBy;
import com.senla.app.service.storage.StorageQueryService;
import com.senla.app.service.storage.StorageService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class StorageController {

    private final StorageService storageService;
    private final StorageQueryService storageQueryService;

    public StorageController(
            StorageService storageService,
            StorageQueryService storageQueryService
    ) {
        this.storageService = storageService;
        this.storageQueryService = storageQueryService;
    }

    @PatchMapping("/{id}/write-off")
    @LoggingOperation("списание книги")
    @PreAuthorize("hasAuthority('SCOPE_book:write_off')")
    public ResponseEntity<BookResponse> writeOffBook(
            @PathVariable Integer id
    ) {
        Book writtenOffBook = storageService.writeOffBook(id);
        return ResponseEntity.ok(new BookResponse(writtenOffBook));
    }

    @PatchMapping("/{id}/add")
    @LoggingOperation("добавление книги")
    @PreAuthorize("hasAuthority('SCOPE_book:add')")
    public ResponseEntity<BookResponse> addBookToStorage(
            @PathVariable Integer id
    ) {
        Book addedBook = storageService.addBookToStorage(id);
        return ResponseEntity.ok(new BookResponse(addedBook));
    }

    @GetMapping({"/", ""})
    @LoggingOperation("получение отсортированных книг")
    @PreAuthorize("hasAuthority('SCOPE_book:view_all')")
    public ResponseEntity<CollectionResponse<BookResponse>> getSorted(
            @RequestParam(defaultValue = "NO_SORT") BookSortBy sort
    ) {
        List<Book> sortedBook = storageQueryService.getSorted(sort);
        return ResponseEntity.ok(new CollectionResponse<>(sortedBook.stream().map(BookResponse::new).toList()));
    }

    @GetMapping("/hard-to-sell")
    @LoggingOperation("получение малопродаваемых книг")
    @PreAuthorize("hasAuthority('SCOPE_book:dead_stock')")
    public ResponseEntity<CollectionResponse<BookResponse>> getHardToSell(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate now
    ) {
        List<Book> hardToSellBooks = storageQueryService.getHardToSell(
                now.getYear(), now.getMonthValue(), now.getDayOfMonth()
        );
        return ResponseEntity.ok(new CollectionResponse<>(hardToSellBooks.stream().map(BookResponse::new).toList()));
    }

    @GetMapping("/{id}/description")
    @LoggingOperation("получение описание книги")
    @PreAuthorize("hasAuthority('SCOPE_book:view_description')")
    public ResponseEntity<String> getBookDescription(
            @PathVariable Integer id
    ) {
        String description = storageQueryService.getBookDescription(id);
        return ResponseEntity.ok(description);
    }
}
