package task.model.comparators.book;

import task.model.entity.Book;

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
