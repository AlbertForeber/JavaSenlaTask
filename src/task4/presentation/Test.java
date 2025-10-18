package task4.presentation;

import task4.data.BookStore;
import task4.data.dto.BookStatus;
import task4.data.dto.OrderStatus;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        BookStore bookStore = new BookStore();

        // ---------------------------------------------------
        System.out.println("Test #1: writeOff + addBook");
        System.out.println(bookStore.bookStorage.getBook("Book1").getStatus());
        bookStore.writeOffBook("Book1");
        System.out.println(bookStore.bookStorage.getBook("Book1").getStatus());
        bookStore.addBookToStorage("Book1");
        System.out.println(bookStore.bookStorage.getBook("Book1").getStatus());

        // ---------------------------------------------------
        System.out.println("\nTest #2: createOrder + order editting");
        System.out.println(bookStore.bookStorage.getBook("Book1").getStatus());
        System.out.println(bookStore.bookStorage.getBook("Book2").getStatus());
        bookStore.createOrder(1, List.of("Book1", "Book2"), "Customer1");
        System.out.println(bookStore.orderManager.getOrder(1).getStatus());
        System.out.println(bookStore.orderManager.getOrder(1).getOrderedBookNames());
        System.out.println(bookStore.orderManager.getOrder(1).getCompletionDate());
        bookStore.orderManager.getOrder(1).setCompletionDate(2025, 18, 10);
        System.out.println(bookStore.orderManager.getOrder(1).getCompletionDate());

        // ---------------------------------------------------
        System.out.println("\nTest #3: cancelOrder");
        System.out.println(bookStore.orderManager.getOrder(1).getStatus());
        bookStore.cancelOrder(1);
        System.out.println(bookStore.orderManager.getOrder(1).getStatus());

        // ---------------------------------------------------
        System.out.println("\nTest #4: changeOrderStatus");
        System.out.println(bookStore.orderManager.getOrder(1).getStatus());
        System.out.println(bookStore.bookStorage.getBook("Book1"));
        bookStore.changeOrderStatus(1, OrderStatus.COMPLETED);
        System.out.println(bookStore.orderManager.getOrder(1).getStatus());
        System.out.println(bookStore.bookStorage.getBook("Book1"));

        // ---------------------------------------------------
        System.out.println("\nTest #5: createRequest");

        bookStore.bookStorage.getBook("Book1").setStatus(BookStatus.SOLD_OUT);

        bookStore.createRequest("Book1");
        bookStore.createRequest("Book1");
        System.out.println(bookStore.requestManager.getRequests());
        bookStore.addBookToStorage("Book1");
        System.out.println(bookStore.requestManager.getRequests());
    }
}
