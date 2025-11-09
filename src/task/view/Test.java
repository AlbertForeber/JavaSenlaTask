package task.old.presentation;

import task.old.data.BookStore;
import task.old.data.dto.sortby.BookSortBy;
import task.old.data.dto.sortby.OrderSortBy;
import task.old.data.dto.sortby.RequestSortBy;
import task.old.data.dto.status.BookStatus;
import task.old.data.dto.status.OrderStatus;
import task.old.utils.ConsoleColors;

import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        BookStore bookStore = new BookStore();

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + ">> Test #1: writeOff + addBook" + ConsoleColors.RESET);
        System.out.println(bookStore.bookStorage.getBook("I_Book1").getStatus());
        bookStore.writeOffBook("I_Book1");
        System.out.println(bookStore.bookStorage.getBook("I_Book1").getStatus());
        bookStore.addBookToStorage("I_Book1");
        System.out.println(bookStore.bookStorage.getBook("I_Book1").getStatus());

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #2: createOrder + order editting" + ConsoleColors.RESET);
        System.out.println(bookStore.bookStorage.getBook("I_Book1").getStatus());
        System.out.println(bookStore.bookStorage.getBook("G_Book2").getStatus());
        bookStore.createOrder(1, List.of("I_Book1", "G_Book2"), "Customer1");
        System.out.println(bookStore.orderManager.getOrder(1).getStatus());
        System.out.println(bookStore.orderManager.getOrder(1).getOrderedBookNames());
        System.out.println(bookStore.orderManager.getOrder(1).getCompletionDate());
        bookStore.orderManager.getOrder(1).setCompletionDate(2025, 12, 10);
        System.out.println(bookStore.orderManager.getOrder(1).getCompletionDate());

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #3: cancelOrder" + ConsoleColors.RESET);
        System.out.println(bookStore.orderManager.getOrder(1).getStatus());
        bookStore.cancelOrder(1);
        System.out.println(bookStore.orderManager.getOrder(1).getStatus());

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #4: changeOrderStatus" + ConsoleColors.RESET);
        System.out.println(bookStore.orderManager.getOrder(1).getStatus());
        System.out.println(bookStore.bookStorage.getBook("I_Book1"));

        bookStore.bookStorage.getBook("I_Book1").setStatus(BookStatus.SOLD_OUT);
        bookStore.changeOrderStatus(1, OrderStatus.COMPLETED);
        System.out.println(bookStore.orderManager.getOrder(1).getStatus());
        System.out.println(bookStore.bookStorage.getBook("I_Book1"));

        bookStore.bookStorage.getBook("I_Book1").setStatus(BookStatus.FREE);
        bookStore.changeOrderStatus(1, OrderStatus.COMPLETED);
        System.out.println(bookStore.orderManager.getOrder(1).getStatus());
        System.out.println(bookStore.bookStorage.getBook("I_Book1"));

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #5: createRequest" + ConsoleColors.RESET);

        bookStore.bookStorage.getBook("I_Book1").setStatus(BookStatus.SOLD_OUT);

        bookStore.createRequest("I_Book1");
        bookStore.createRequest("I_Book1");
        System.out.println(bookStore.requestManager.getRequests());
        bookStore.addBookToStorage("I_Book1");
        System.out.println(bookStore.requestManager.getRequests());

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #6: Book Sorts" + ConsoleColors.RESET );

        System.out.println(ConsoleColors.BLUE + "\n NO SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStore.getSorted(BookSortBy.NO_SORT)));

        System.out.println(ConsoleColors.BLUE + "\n TITLE SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStore.getSorted(BookSortBy.TITLE)));

        System.out.println(ConsoleColors.BLUE + "\n ADMISSION DATE SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStore.getSorted(BookSortBy.ADMISSION_DATE)));

        System.out.println(ConsoleColors.BLUE + "\n PUBLICATION DATE SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStore.getSorted(BookSortBy.PUBLICATION_DATE)));

        System.out.println(ConsoleColors.BLUE + "\n PRICE SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStore.getSorted(BookSortBy.PRICE)));

        System.out.println(ConsoleColors.BLUE + "\n AVAILABILITY SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStore.getSorted(BookSortBy.AVAILABILITY)));

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #7: Order Sorts" + ConsoleColors.RESET);

        bookStore.createOrder(2, List.of("I_Book1", "F_Book3"), "John Doe");
        bookStore.orderManager.getOrder(2).setCompletionDate(2026, 1, 2);
        bookStore.changeOrderStatus(2, OrderStatus.COMPLETED);

        System.out.println(ConsoleColors.BLUE + "\n NO SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStore.getSorted(OrderSortBy.NO_SORT)));

        System.out.println(ConsoleColors.BLUE + "\n PRICE SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStore.getSorted(OrderSortBy.PRICE)));

        System.out.println(ConsoleColors.BLUE + "\n STATUS SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStore.getSorted(OrderSortBy.STATUS)));

        System.out.println(ConsoleColors.BLUE + "\n COMPLETION DATE SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStore.getSorted(OrderSortBy.COMPLETION_DATE)));

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #8: Request Sorts" + ConsoleColors.RESET);
        bookStore.createRequest("E_Book7");
        bookStore.createRequest("B_Book8");
        bookStore.createRequest("B_Book8");
        bookStore.createRequest("B_Book8");
        bookStore.createRequest("D_Book6");
        bookStore.createRequest("D_Book6");

        System.out.println(ConsoleColors.BLUE + "\n NO SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStore.getSorted(RequestSortBy.NO_SORT)));

        System.out.println(ConsoleColors.BLUE + "\n BOOK NAME SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStore.getSorted(RequestSortBy.BOOK_NAME)));

        System.out.println(ConsoleColors.BLUE + "\n AMOUNT SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStore.getSorted(RequestSortBy.AMOUNT)));

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #9: Interval Check" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE + "\n FROM 1.6.2025 TO 31.12.2025" + ConsoleColors.RESET);

        bookStore.changeOrderStatus(2, OrderStatus.COMPLETED);
        System.out.println(bookStore.orderManager.getOrder(2));

        System.out.println(
                Arrays.toString(
                        bookStore.
                                getCompletedOrdersInInterval(
                                        2025,
                                        6,
                                        1,
                                        2025,
                                        12,
                                        31
                                )
                )
        );

        System.out.println(ConsoleColors.BLUE + "\n FROM 1.1.2026 TO 31.12.26" + ConsoleColors.RESET);
        System.out.println(
                Arrays.toString(
                        bookStore.
                                getCompletedOrdersInInterval(
                                        2026,
                                        1,
                                        1,
                                        2026,
                                        12,
                                        31
                                )
                )
        );

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #10: Total Income" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE + "\n FROM 1.6.2025 TO 31.12.2025" + ConsoleColors.RESET);
        System.out.println(
                    bookStore.
                            getIncomeInInterval(
                                    2025,
                                    6,
                                    1,
                                    2025,
                                    12,
                                    31
                            )
        );

        System.out.println(ConsoleColors.BLUE + "\n FROM 1.1.2026 TO 31.12.26" + ConsoleColors.RESET);
        System.out.println(
                    bookStore.
                            getIncomeInInterval(
                                    2026,
                                    1,
                                    1,
                                    2026,
                                    12,
                                    31
                            )
        );

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #11: Total Orders" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE + "\n FROM 1.6.2025 TO 31.12.2025" + ConsoleColors.RESET);
        System.out.println(
                bookStore.
                        getOrderAmountInInterval(
                                2025,
                                6,
                                1,
                                2025,
                                12,
                                31
                        )
        );

        System.out.println(ConsoleColors.BLUE + "\n FROM 1.6.2025 TO 31.12.26" + ConsoleColors.RESET);
        System.out.println(
                bookStore.
                        getOrderAmountInInterval(
                                2025,
                                6,
                                1,
                                2026,
                                12,
                                31
                        )
        );

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #12: Hard to sell check" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE + "\n ALL BOOKS SORTED BY ADMISSION DATE" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStore.getSorted(BookSortBy.ADMISSION_DATE)));
        System.out.println(ConsoleColors.BLUE + "\n HARD TO SELL BOOKS" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStore.getHardToSell(2025, 11, 1)));

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #12: Order details" + ConsoleColors.RESET);
        System.out.println(bookStore.orderManager.getOrder(2));

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #13: Book description (I_Book1)" + ConsoleColors.RESET);
        System.out.println(bookStore.getBookDescription("I_Book1"));
    }
}
