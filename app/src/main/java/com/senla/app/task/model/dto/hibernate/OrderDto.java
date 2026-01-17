package com.senla.app.task.model.dto.hibernate;

import com.senla.app.task.model.entity.status.OrderStatus;
import jakarta.persistence.*;

import java.util.Calendar;
import java.util.List;

@Entity(name = "orders")
public class OrderDto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @SequenceGenerator(
            name = "order_seq",
            sequenceName = "orders_id_seq",
            allocationSize = 1
    )
    private int id;

    @OneToMany(mappedBy = "orderDto")
    private List<Integer> orderedBookIds;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "total_sum")
    private int totalSum;

    @Temporal(TemporalType.DATE)
    @Column(name = "completion_date")
    private Calendar completionDate = null;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;
}
