package com.senla.app.task.model.comparators.request;

import com.senla.app.task.model.entity.Request;

import java.util.Comparator;

public class RequestBookNameComparator implements Comparator<Request> {

    @Override
    public int compare(Request o1, Request o2) {
        return o1.getBook().getTitle().compareTo(o2.getBook().getTitle());
    }
}
