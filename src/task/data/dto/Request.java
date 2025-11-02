package task4.data.dto;

public class Request {
    private final String bookName;

    public Request(String bookName) {
        this.bookName = bookName;
    }

    public String getBookName() {
        return bookName;
    }
}
