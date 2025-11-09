package task.model.comparators.request;

import task.model.entity.Request;

import java.util.Comparator;

public class RequestAmountComparator implements Comparator<Request> {
    @Override
    public int compare(Request o1, Request o2) {
        return Integer.compare(o1.getAmount(), o2.getAmount());
    }
}
