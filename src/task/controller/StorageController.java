package task.controller;

import task.model.entity.sortby.BookSortBy;
import task.service.facade.BookStorageFacade;

public class StorageController {
    BookStorageFacade bookStorageFacade;

    public StorageController(BookStorageFacade bookStorageFacade) {
        this.bookStorageFacade = bookStorageFacade;
    }

    public void writeOffBook(String bookName) {
        bookStorageFacade.writeOffBook(bookName);
    }

    public void addBookToStorage(String bookName) {
        bookStorageFacade.addBookToStorage(bookName);
    }

    public void getSorted(BookSortBy sortBy) {
        bookStorageFacade.getSorted(sortBy);
    }

    public void getHardToSell(
            int nowYear,
            int nowMonth,
            int nowDate
    ) {
        bookStorageFacade.getHardToSell(nowYear, nowMonth, nowDate);
    }

    public void getBookDescription(String bookName) {
        bookStorageFacade.getBookDescription(bookName);
    }
}
