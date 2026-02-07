package com.senla.app.model.entity;

import com.senla.app.model.entity.status.OrderStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Entity(name = "orders")
public class Order implements Serializable {

    @Id
//    Так как заказы добавляются вместе с ID добавление авто-генерации сломает систему
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
//    @SequenceGenerator(name = "order_seq", sequenceName = "orders_id_seq", allocationSize = 1)
    private int id;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "book_order",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id")
    )
    private List<Book> orderedBooks;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "total_sum")
    private int totalSum;

    @Temporal(TemporalType.DATE)
    @Column(name = "completion_date")
    private Calendar completionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    protected Order() { }

    public Order(int id, List<Book> orderedBooks, int totalSum, String customerName) {
        this.id = id;
        this.orderedBooks = orderedBooks;
        this.customerName = customerName;
        this.totalSum = totalSum;
        status = OrderStatus.NEW;
    }

    public Order(int id, List<Book> orderedBooks, String customerName, int totalSum, Calendar completionDate, OrderStatus status) {
        this.id = id;
        this.orderedBooks = orderedBooks;
        this.customerName = customerName;
        this.totalSum = totalSum;
        this.completionDate = completionDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public List<Book> getOrderedBooks() {
        return orderedBooks;
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
                    bookIds: %s,
                    customerName: %s,
                    completionDate: %d %d %d
                    totalSum: %d,
                    status: %s
                """,
                id,
                orderedBooks != null ? orderedBooks.stream().map(Book::getId).toList() : "NOT RECEIVED",
                customerName,
                completionDate != null ? completionDate.get(Calendar.YEAR) : 0,
                completionDate != null ? completionDate.get(Calendar.MONTH) + 1 : 0,
                completionDate != null ? completionDate.get(Calendar.DATE) : 0,
                totalSum,
                status
                );
    }
}
