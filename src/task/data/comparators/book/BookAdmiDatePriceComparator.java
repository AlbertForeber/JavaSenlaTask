package task.data.comparators.book;

import task.data.comparators.order.OrderComplDateComparator;
import task.data.comparators.order.OrderPriceComparator;
import task.data.dto.Book;
import task.data.dto.Order;

import java.util.Comparator;

public class BookAdmiDatePriceComparator implements Comparator<Book> {
    Comparator<Book> c1 = new BookAdmiDateComparator();
    Comparator<Book> c2 = new BookPriceComparator();

    @Override
    public int compare(Book o1, Book o2) {
        int result = c1.compare(o1, o2);

        if (result != 0) {
            return result;
        }

        return c2.compare(o1, o2);
    }
}
