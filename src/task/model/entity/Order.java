package task.old.data.dto;

import task.old.data.dto.status.OrderStatus;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Order {
    private final int id;
    private final List<String> orderedBookNames;
    private final String customerName;
    private final int totalSum;
    private Calendar completionDate = null;
    private OrderStatus status;

    
    public Order(int id, List<String> bookNames, int totalSum, String customerName) {
        this.id = id;
        this.orderedBookNames = bookNames;
        this.customerName = customerName;
        this.totalSum = totalSum;
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
        if (completionDate == null) completionDate = new GregorianCalendar();
        completionDate.set(year, month - 1, date);
    }

    public Calendar getCompletionDate() {
        return completionDate;
    }

    public int getTotalSum() {
        return totalSum;
    }


    @Override
    public String toString() {
        return String.format("""
                ORDER:
                    id: %d,
                    bookNames: %s,
                    customerName: %s,
                    completionDate: %d %d %d
                    totalSum: %d,
                    status: %s
                """,
                id,
                orderedBookNames,
                customerName,
                completionDate.get(Calendar.YEAR),
                completionDate.get(Calendar.MONTH) + 1,
                completionDate.get(Calendar.DATE),
                totalSum,
                status
                );
    }
}
