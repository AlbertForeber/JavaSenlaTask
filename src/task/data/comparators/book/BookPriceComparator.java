package task.data.comparators.book;

import task.data.dto.Book;

import java.util.Comparator;

public class BookPriceComparator implements Comparator<Book> {
    @Override
    public int compare(Book o1, Book o2) {
        return Integer.compare(o1.getPrice(), o2.getPrice());
    }
}
