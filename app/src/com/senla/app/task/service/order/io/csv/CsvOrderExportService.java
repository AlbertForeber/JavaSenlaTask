package task.service.order.io.csv;

import task.model.entity.Order;
import task.repository.OrderManagerRepository;
import task.service.order.io.OrderExportService;
import task.utils.DataConverter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.stream.Collectors;

public class CsvOrderExportService implements OrderExportService {
    private final OrderManagerRepository orderManagerRepository;
    private Order order;

    public CsvOrderExportService(OrderManagerRepository orderManagerRepository) {
        this.orderManagerRepository = orderManagerRepository;
    }

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
