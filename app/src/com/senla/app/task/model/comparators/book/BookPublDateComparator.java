package com.senla.app.task.model.comparators.book;

import com.senla.app.task.model.entity.Book;

import java.util.Comparator;

public class BookPublDateComparator implements Comparator<Book> {
    @Override
    public int compare(Book o1, Book o2) {
        return o1.getPublicationDate().compareTo(o2.getPublicationDate());
    }
}
