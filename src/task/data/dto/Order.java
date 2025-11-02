package task4.data.dto;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Order {
    private final int id;
    private final List<String> orderedBookNames;
    private final String customerName;
    private final Calendar completionDate = new GregorianCalendar();
    private OrderStatus status;

    
    public Order(int id, List<String> bookNames, String customerName) {
        this.id = id;
        this.orderedBookNames = bookNames;
        this.customerName = customerName;
        status = OrderStatus.NEW;
    }

    public int getId() {
        return id;
    }

    public List<String> getOrderedBookNames() {
        return orderedBookNames;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCompletionDate(int year, int month, int date) {
        completionDate.set(year, month, date);
    }

    public Calendar getCompletionDate() {
        return completionDate;
    }
}
