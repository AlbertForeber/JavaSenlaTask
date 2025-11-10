package task.view;

import task.service.facade.old.BookStoreFacade;
import task.model.entity.sortby.BookSortBy;
import task.model.entity.sortby.OrderSortBy;
import task.model.entity.sortby.RequestSortBy;
import task.model.entity.status.BookStatus;
import task.model.entity.status.OrderStatus;
import task.utils.Colors;

import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        BookStoreFacade bookStoreFacade = new BookStoreFacade();

        // ---------------------------------------------------
        System.out.println(Colors.YELLOW + ">> Test #1: writeOff + addBook" + Colors.RESET);
        System.out.println(bookStoreFacade.bookStorageRepository.getBook("I_Book1").getStatus());
        bookStoreFacade.writeOffBook("I_Book1");
        System.out.println(bookStoreFacade.bookStorageRepository.getBook("I_Book1").getStatus());
        bookStoreFacade.addBookToStorage("I_Book1");
        System.out.println(bookStoreFacade.bookStorageRepository.getBook("I_Book1").getStatus());

        // ---------------------------------------------------
        System.out.println(Colors.YELLOW + "\n>> Test #2: createOrder + order editting" + Colors.RESET);
        System.out.println(bookStoreFacade.bookStorageRepository.getBook("I_Book1").getStatus());
        System.out.println(bookStoreFacade.bookStorageRepository.getBook("G_Book2").getStatus());
        bookStoreFacade.createOrder(1, List.of("I_Book1", "G_Book2"), "Customer1");
        System.out.println(bookStoreFacade.orderManagerService.getOrder(1).getStatus());
        System.out.println(bookStoreFacade.orderManagerService.getOrder(1).getOrderedBookNames());
        System.out.println(bookStoreFacade.orderManagerService.getOrder(1).getCompletionDate());
        bookStoreFacade.orderManagerService.getOrder(1).setCompletionDate(2025, 12, 10);
        System.out.println(bookStoreFacade.orderManagerService.getOrder(1).getCompletionDate());

        // ---------------------------------------------------
        System.out.println(Colors.YELLOW + "\n>> Test #3: cancelOrder" + Colors.RESET);
        System.out.println(bookStoreFacade.orderManagerService.getOrder(1).getStatus());
        bookStoreFacade.cancelOrder(1);
        System.out.println(bookStoreFacade.orderManagerService.getOrder(1).getStatus());

        // ---------------------------------------------------
        System.out.println(Colors.YELLOW + "\n>> Test #4: changeOrderStatus" + Colors.RESET);
        System.out.println(bookStoreFacade.orderManagerService.getOrder(1).getStatus());
        System.out.println(bookStoreFacade.bookStorageRepository.getBook("I_Book1"));

        bookStoreFacade.bookStorageRepository.getBook("I_Book1").setStatus(BookStatus.SOLD_OUT);
        bookStoreFacade.changeOrderStatus(1, OrderStatus.COMPLETED);
        System.out.println(bookStoreFacade.orderManagerService.getOrder(1).getStatus());
        System.out.println(bookStoreFacade.bookStorageRepository.getBook("I_Book1"));

        bookStoreFacade.bookStorageRepository.getBook("I_Book1").setStatus(BookStatus.FREE);
        bookStoreFacade.changeOrderStatus(1, OrderStatus.COMPLETED);
        System.out.println(bookStoreFacade.orderManagerService.getOrder(1).getStatus());
        System.out.println(bookStoreFacade.bookStorageRepository.getBook("I_Book1"));

        // ---------------------------------------------------
        System.out.println(Colors.YELLOW + "\n>> Test #5: createRequest" + Colors.RESET);

        bookStoreFacade.bookStorageRepository.getBook("I_Book1").setStatus(BookStatus.SOLD_OUT);

        bookStoreFacade.createRequest("I_Book1");
        bookStoreFacade.createRequest("I_Book1");
        System.out.println(bookStoreFacade.requestManagerRepository.getRequests());
        bookStoreFacade.addBookToStorage("I_Book1");
        System.out.println(bookStoreFacade.requestManagerRepository.getRequests());

        // ---------------------------------------------------
        System.out.println(Colors.YELLOW + "\n>> Test #6: Book Sorts" + Colors.RESET );

        System.out.println(Colors.BLUE + "\n NO SORT" + Colors.RESET);
        System.out.println(bookStoreFacade.getSorted(BookSortBy.NO_SORT));

        System.out.println(Colors.BLUE + "\n TITLE SORT" + Colors.RESET);
        System.out.println(bookStoreFacade.getSorted(BookSortBy.TITLE));

        System.out.println(Colors.BLUE + "\n ADMISSION DATE SORT" + Colors.RESET);
        System.out.println((bookStoreFacade.getSorted(BookSortBy.ADMISSION_DATE)));

        System.out.println(Colors.BLUE + "\n PUBLICATION DATE SORT" + Colors.RESET);
        System.out.println((bookStoreFacade.getSorted(BookSortBy.PUBLICATION_DATE)));

        System.out.println(Colors.BLUE + "\n PRICE SORT" + Colors.RESET);
        System.out.println((bookStoreFacade.getSorted(BookSortBy.PRICE)));

        System.out.println(Colors.BLUE + "\n AVAILABILITY SORT" + Colors.RESET);
        System.out.println((bookStoreFacade.getSorted(BookSortBy.AVAILABILITY)));

        // ---------------------------------------------------
        System.out.println(Colors.YELLOW + "\n>> Test #7: Order Sorts" + Colors.RESET);

        bookStoreFacade.createOrder(2, List.of("I_Book1", "F_Book3"), "John Doe");
        bookStoreFacade.orderManagerService.getOrder(2).setCompletionDate(2026, 1, 2);
        bookStoreFacade.changeOrderStatus(2, OrderStatus.COMPLETED);

        System.out.println(Colors.BLUE + "\n NO SORT" + Colors.RESET);
        System.out.println((bookStoreFacade.getSorted(OrderSortBy.NO_SORT)));

        System.out.println(Colors.BLUE + "\n PRICE SORT" + Colors.RESET);
        System.out.println((bookStoreFacade.getSorted(OrderSortBy.PRICE)));

        System.out.println(Colors.BLUE + "\n STATUS SORT" + Colors.RESET);
        System.out.println((bookStoreFacade.getSorted(OrderSortBy.STATUS)));

        System.out.println(Colors.BLUE + "\n COMPLETION DATE SORT" + Colors.RESET);
        System.out.println((bookStoreFacade.getSorted(OrderSortBy.COMPLETION_DATE)));

        // ---------------------------------------------------
        System.out.println(Colors.YELLOW + "\n>> Test #8: Request Sorts" + Colors.RESET);
        bookStoreFacade.createRequest("E_Book7");
        bookStoreFacade.createRequest("B_Book8");
        bookStoreFacade.createRequest("B_Book8");
        bookStoreFacade.createRequest("B_Book8");
        bookStoreFacade.createRequest("D_Book6");
        bookStoreFacade.createRequest("D_Book6");

        System.out.println(Colors.BLUE + "\n NO SORT" + Colors.RESET);
        System.out.println((bookStoreFacade.getSorted(RequestSortBy.NO_SORT)));

        System.out.println(Colors.BLUE + "\n BOOK NAME SORT" + Colors.RESET);
        System.out.println((bookStoreFacade.getSorted(RequestSortBy.BOOK_NAME)));

        System.out.println(Colors.BLUE + "\n AMOUNT SORT" + Colors.RESET);
        System.out.println((bookStoreFacade.getSorted(RequestSortBy.AMOUNT)));

        // ---------------------------------------------------
        System.out.println(Colors.YELLOW + "\n>> Test #9: Interval Check" + Colors.RESET);
        System.out.println(Colors.BLUE + "\n FROM 1.6.2025 TO 31.12.2025" + Colors.RESET);

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

        System.out.println(Colors.BLUE + "\n FROM 1.1.2026 TO 31.12.26" + Colors.RESET);
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
        System.out.println(Colors.YELLOW + "\n>> Test #10: Total Income" + Colors.RESET);
        System.out.println(Colors.BLUE + "\n FROM 1.6.2025 TO 31.12.2025" + Colors.RESET);
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

        System.out.println(Colors.BLUE + "\n FROM 1.1.2026 TO 31.12.26" + Colors.RESET);
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
        System.out.println(Colors.YELLOW + "\n>> Test #11: Total Orders" + Colors.RESET);
        System.out.println(Colors.BLUE + "\n FROM 1.6.2025 TO 31.12.2025" + Colors.RESET);
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

        System.out.println(Colors.BLUE + "\n FROM 1.6.2025 TO 31.12.26" + Colors.RESET);
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
        System.out.println(Colors.YELLOW + "\n>> Test #12: Hard to sell check" + Colors.RESET);
        System.out.println(Colors.BLUE + "\n ALL BOOKS SORTED BY ADMISSION DATE" + Colors.RESET);
        System.out.println((bookStoreFacade.getSorted(BookSortBy.ADMISSION_DATE)));
        System.out.println(Colors.BLUE + "\n HARD TO SELL BOOKS" + Colors.RESET);
        System.out.println(Arrays.toString(bookStoreFacade.getHardToSell(2025, 11, 1)));

        // ---------------------------------------------------
        System.out.println(Colors.YELLOW + "\n>> Test #12: Order details" + Colors.RESET);
        System.out.println(bookStoreFacade.orderManagerService.getOrder(2));

        // ---------------------------------------------------
        System.out.println(Colors.YELLOW + "\n>> Test #13: Book description (I_Book1)" + Colors.RESET);
        System.out.println(bookStoreFacade.getBookDescription("I_Book1"));
    }
}
