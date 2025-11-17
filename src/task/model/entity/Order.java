package task.model.entity;

import task.model.entity.status.OrderStatus;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Order {
    private final int id;
    private final List<Integer> orderedBookIds;
    private final String customerName;
    private int totalSum;
    private Calendar completionDate = null;
    private OrderStatus status;

    
    public Order(int id, List<Integer> bookIds, int totalSum, String customerName) {
        this.id = id;
        this.orderedBookIds = bookIds;
        this.customerName = customerName;
        this.totalSum = totalSum;
        status = OrderStatus.NEW;

    }

    public int getId() {
        return id;
    }

    public List<Integer> getOrderedBookIds() {
        return orderedBookIds;
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
    public void setTotalSum(int totalSum) {
        this.totalSum = totalSum;
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
                orderedBookIds,
                customerName,
                completionDate != null ? completionDate.get(Calendar.YEAR) : 0,
                completionDate != null ? completionDate.get(Calendar.MONTH) + 1 : 0,
                completionDate != null ? completionDate.get(Calendar.DATE) : 0,
                totalSum,
                status
                );
    }
}
