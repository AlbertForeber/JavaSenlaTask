package task.view;

import task.service.facade.old.BookStoreFacade;
import task.model.entity.sortby.BookSortBy;
import task.model.entity.sortby.OrderSortBy;
import task.model.entity.sortby.RequestSortBy;
import task.model.entity.status.BookStatus;
import task.model.entity.status.OrderStatus;
import task.utils.ConsoleColors;

import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        BookStoreFacade bookStoreFacade = new BookStoreFacade();

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + ">> Test #1: writeOff + addBook" + ConsoleColors.RESET);
        System.out.println(bookStoreFacade.bookStorageService.getBook("I_Book1").getStatus());
        bookStoreFacade.writeOffBook("I_Book1");
        System.out.println(bookStoreFacade.bookStorageService.getBook("I_Book1").getStatus());
        bookStoreFacade.addBookToStorage("I_Book1");
        System.out.println(bookStoreFacade.bookStorageService.getBook("I_Book1").getStatus());

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #2: createOrder + order editting" + ConsoleColors.RESET);
        System.out.println(bookStoreFacade.bookStorageService.getBook("I_Book1").getStatus());
        System.out.println(bookStoreFacade.bookStorageService.getBook("G_Book2").getStatus());
        bookStoreFacade.createOrder(1, List.of("I_Book1", "G_Book2"), "Customer1");
        System.out.println(bookStoreFacade.orderManagerService.getOrder(1).getStatus());
        System.out.println(bookStoreFacade.orderManagerService.getOrder(1).getOrderedBookNames());
        System.out.println(bookStoreFacade.orderManagerService.getOrder(1).getCompletionDate());
        bookStoreFacade.orderManagerService.getOrder(1).setCompletionDate(2025, 12, 10);
        System.out.println(bookStoreFacade.orderManagerService.getOrder(1).getCompletionDate());

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #3: cancelOrder" + ConsoleColors.RESET);
        System.out.println(bookStoreFacade.orderManagerService.getOrder(1).getStatus());
        bookStoreFacade.cancelOrder(1);
        System.out.println(bookStoreFacade.orderManagerService.getOrder(1).getStatus());

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #4: changeOrderStatus" + ConsoleColors.RESET);
        System.out.println(bookStoreFacade.orderManagerService.getOrder(1).getStatus());
        System.out.println(bookStoreFacade.bookStorageService.getBook("I_Book1"));

        bookStoreFacade.bookStorageService.getBook("I_Book1").setStatus(BookStatus.SOLD_OUT);
        bookStoreFacade.changeOrderStatus(1, OrderStatus.COMPLETED);
        System.out.println(bookStoreFacade.orderManagerService.getOrder(1).getStatus());
        System.out.println(bookStoreFacade.bookStorageService.getBook("I_Book1"));

        bookStoreFacade.bookStorageService.getBook("I_Book1").setStatus(BookStatus.FREE);
        bookStoreFacade.changeOrderStatus(1, OrderStatus.COMPLETED);
        System.out.println(bookStoreFacade.orderManagerService.getOrder(1).getStatus());
        System.out.println(bookStoreFacade.bookStorageService.getBook("I_Book1"));

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #5: createRequest" + ConsoleColors.RESET);

        bookStoreFacade.bookStorageService.getBook("I_Book1").setStatus(BookStatus.SOLD_OUT);

        bookStoreFacade.createRequest("I_Book1");
        bookStoreFacade.createRequest("I_Book1");
        System.out.println(bookStoreFacade.requestManagerService.getRequests());
        bookStoreFacade.addBookToStorage("I_Book1");
        System.out.println(bookStoreFacade.requestManagerService.getRequests());

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #6: Book Sorts" + ConsoleColors.RESET );

        System.out.println(ConsoleColors.BLUE + "\n NO SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStoreFacade.getSorted(BookSortBy.NO_SORT)));

        System.out.println(ConsoleColors.BLUE + "\n TITLE SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStoreFacade.getSorted(BookSortBy.TITLE)));

        System.out.println(ConsoleColors.BLUE + "\n ADMISSION DATE SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStoreFacade.getSorted(BookSortBy.ADMISSION_DATE)));

        System.out.println(ConsoleColors.BLUE + "\n PUBLICATION DATE SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStoreFacade.getSorted(BookSortBy.PUBLICATION_DATE)));

        System.out.println(ConsoleColors.BLUE + "\n PRICE SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStoreFacade.getSorted(BookSortBy.PRICE)));

        System.out.println(ConsoleColors.BLUE + "\n AVAILABILITY SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStoreFacade.getSorted(BookSortBy.AVAILABILITY)));

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #7: Order Sorts" + ConsoleColors.RESET);

        bookStoreFacade.createOrder(2, List.of("I_Book1", "F_Book3"), "John Doe");
        bookStoreFacade.orderManagerService.getOrder(2).setCompletionDate(2026, 1, 2);
        bookStoreFacade.changeOrderStatus(2, OrderStatus.COMPLETED);

        System.out.println(ConsoleColors.BLUE + "\n NO SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStoreFacade.getSorted(OrderSortBy.NO_SORT)));

        System.out.println(ConsoleColors.BLUE + "\n PRICE SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStoreFacade.getSorted(OrderSortBy.PRICE)));

        System.out.println(ConsoleColors.BLUE + "\n STATUS SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStoreFacade.getSorted(OrderSortBy.STATUS)));

        System.out.println(ConsoleColors.BLUE + "\n COMPLETION DATE SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStoreFacade.getSorted(OrderSortBy.COMPLETION_DATE)));

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #8: Request Sorts" + ConsoleColors.RESET);
        bookStoreFacade.createRequest("E_Book7");
        bookStoreFacade.createRequest("B_Book8");
        bookStoreFacade.createRequest("B_Book8");
        bookStoreFacade.createRequest("B_Book8");
        bookStoreFacade.createRequest("D_Book6");
        bookStoreFacade.createRequest("D_Book6");

        System.out.println(ConsoleColors.BLUE + "\n NO SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStoreFacade.getSorted(RequestSortBy.NO_SORT)));

        System.out.println(ConsoleColors.BLUE + "\n BOOK NAME SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStoreFacade.getSorted(RequestSortBy.BOOK_NAME)));

        System.out.println(ConsoleColors.BLUE + "\n AMOUNT SORT" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStoreFacade.getSorted(RequestSortBy.AMOUNT)));

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #9: Interval Check" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE + "\n FROM 1.6.2025 TO 31.12.2025" + ConsoleColors.RESET);

        bookStoreFacade.changeOrderStatus(2, OrderStatus.COMPLETED);
        System.out.println(bookStoreFacade.orderManagerService.getOrder(2));

        System.out.println(
                Arrays.toString(
                        bookStoreFacade.
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
                        bookStoreFacade.
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
                    bookStoreFacade.
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
                    bookStoreFacade.
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
                bookStoreFacade.
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
                bookStoreFacade.
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
        System.out.println(Arrays.toString(bookStoreFacade.getSorted(BookSortBy.ADMISSION_DATE)));
        System.out.println(ConsoleColors.BLUE + "\n HARD TO SELL BOOKS" + ConsoleColors.RESET);
        System.out.println(Arrays.toString(bookStoreFacade.getHardToSell(2025, 11, 1)));

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #12: Order details" + ConsoleColors.RESET);
        System.out.println(bookStoreFacade.orderManagerService.getOrder(2));

        // ---------------------------------------------------
        System.out.println(ConsoleColors.YELLOW + "\n>> Test #13: Book description (I_Book1)" + ConsoleColors.RESET);
        System.out.println(bookStoreFacade.getBookDescription("I_Book1"));
    }
}
