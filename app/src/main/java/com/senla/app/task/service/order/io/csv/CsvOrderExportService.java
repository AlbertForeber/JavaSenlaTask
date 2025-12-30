package com.senla.app.task.service.order.io.csv;

import com.senla.annotation.InjectTo;
import com.senla.app.task.model.entity.Order;
import com.senla.app.task.repository.OrderManagerRepository;
import com.senla.app.task.repository.inmemory.InMemoryOrderManagerRepository;
import com.senla.app.task.service.order.io.OrderExportService;
import com.senla.app.task.utils.DataConverter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.stream.Collectors;

public class CsvOrderExportService implements OrderExportService {

    @InjectTo(useImplementation = InMemoryOrderManagerRepository.class)
    private OrderManagerRepository orderManagerRepository;

    private Order order;

    public CsvOrderExportService() {}

    @Override
    public void exportOrder(int orderId, String path) throws IllegalArgumentException, IOException {
        Order order = orderManagerRepository.getOrder(orderId);

        if (!path.endsWith("\\") && !path.endsWith("/")) {
            throw new IllegalArgumentException("Неверно указан путь");
        }

        if (order == null) throw new IllegalArgumentException("Заказ с ID = " + orderId + " не найден");
        writeOrder(path + "order_" + orderId + ".csv", order);
    }

    public void writeOrder(String fileName, Order order) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {

            bw.write("id;bookIds;customerName;totalSum;completionDate;status");
            bw.newLine();
            bw.write(order.getId() + ";");

            bw.write(
                    order.getOrderedBookIds()
                            .stream()
                            .map(x -> Integer.toString(x))
                            .collect(Collectors.joining(","))
            + ";");

            bw.write(order.getCustomerName() + ";");
            bw.write(Long.toString(order.getTotalSum()) + ";");

            Calendar completionDate = order.getCompletionDate();
            bw.write(completionDate != null ? (DataConverter.calendarToString(completionDate) + ";") : ";");

            bw.write(order.getStatus().name());

        }
    }
}
