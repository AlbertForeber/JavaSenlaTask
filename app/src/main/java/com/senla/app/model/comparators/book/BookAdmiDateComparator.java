package com.senla.app.model.comparators.book;

import com.senla.app.model.entity.Book;

import java.util.Comparator;

public class BookAdmiDateComparator implements Comparator<Book> {

    @Override
    public int compare(Book o1, Book o2) {
        return o1.getAdmissionDate().compareTo(o2.getAdmissionDate());
    }
}
