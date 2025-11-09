package task.controller;

import task.model.entity.Request;
import task.model.entity.sortby.RequestSortBy;
import task.service.facade.BookRequestFacade;

public class RequestController {
    BookRequestFacade bookRequestFacade;

    public RequestController(BookRequestFacade bookRequestFacade) {
        this.bookRequestFacade = bookRequestFacade;
    }

    public void createRequest(String bookName) {
        bookRequestFacade.createRequest(bookName);
    }

    public void getSorted(RequestSortBy sortBy) {
        bookRequestFacade.getSorted(sortBy);
    }

}
