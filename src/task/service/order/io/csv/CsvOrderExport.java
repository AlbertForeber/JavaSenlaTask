package task.service.order.io.csv;

import task.model.entity.Order;
import task.repository.OrderManagerRepository;
import task.service.order.io.OrderExport;
import task.utils.DataConverter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class CsvOrderExport implements OrderExport {
    private final OrderManagerRepository orderManagerRepository;
    private Order order;

    public CsvOrderExport(OrderManagerRepository orderManagerRepository) {
        this.orderManagerRepository = orderManagerRepository;
    }

    @Override
    public void exportOrder(int orderId) throws IllegalArgumentException, IOException {
        Order order = orderManagerRepository.getOrder(orderId);

        if (order == null) throw new IllegalArgumentException("Заказ с ID = " + orderId + " не найден");
        writeOrder("order_" + orderId, order);
    }

    public void writeOrder(String fileName, Order order) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {

            bw.write("id;" + order.getId());
            bw.newLine();

            bw.write("bookNames;" + String.join(",", order.getOrderedBookNames()));
            bw.newLine();

            bw.write("customerName;" + order.getCustomerName());
            bw.newLine();

            bw.write("totalSum;" + Long.toString(order.getTotalSum()));
            bw.newLine();

            Calendar completionDate = order.getCompletionDate();
            bw.write("completionDate;" +
                    (completionDate != null ? DataConverter.calendarToString(completionDate) : "null")
            );
            bw.newLine();

            bw.write("status;" + order.getStatus().name());

        }
    }
}
