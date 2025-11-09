package task.old.data.comparators.order;

import task.old.data.dto.Order;

import java.util.Comparator;

public class OrderComplDateComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return o1.getCompletionDate().compareTo(o2.getCompletionDate());
    }
}
