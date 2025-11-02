package task.presentation;

import task.data.BookStore;
import task.data.dto.sortby.BookSortBy;

import java.util.Arrays;

public class Container {
    public static void main(String[] args) {
        BookStore bookStore = new BookStore();
        System.out.println(Arrays.toString(bookStore.bookStorage.getSortedBooks(BookSortBy.PUBLICATION_DATE)));
    }
}
