package task.old.data.comparators.book;

import task.old.data.dto.Book;

import java.util.Comparator;

public class BookAvailabilityComparator implements Comparator<Book> {
    @Override
    public int compare(Book o1, Book o2) {
        return o1.getStatus().compareTo(o2.getStatus());
    }
}
