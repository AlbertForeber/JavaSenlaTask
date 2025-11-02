package task.data.comparators.order;

import task.data.dto.Order;

import java.util.Comparator;

public class OrderComplDatePriceComparator implements Comparator<Order> {
    Comparator<Order> c1 = new OrderComplDateComparator();
    Comparator<Order> c2 = new OrderPriceComparator();

    @Override
    public int compare(Order o1, Order o2) {
        int result = c1.compare(o1, o2);

        if (result != 0) {
            return result;
        }

        return c2.compare(o1, o2);
    }
}
