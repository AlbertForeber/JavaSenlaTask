package task.data.dto;

import task.data.dto.status.OrderStatus;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Order {
    private final int id;
    private final List<String> orderedBookNames;
    private final String customerName;
    private final Calendar completionDate = new GregorianCalendar();
    private final int totalSum;
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
        completionDate.set(year, month, date);
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
                    completionDate: %s,
                    totalSum: %d,
                    status: %s
                """,
                id,
                orderedBookNames,
                customerName,
                completionDate,
                totalSum,
                status
                );
    }
}
