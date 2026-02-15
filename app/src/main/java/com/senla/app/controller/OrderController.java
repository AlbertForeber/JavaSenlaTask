package com.senla.app.controller;

import com.senla.annotation.LoggingOperation;
import com.senla.app.exceptions.ResourceNotFound;
import com.senla.app.model.dto.request.CreateOrderRequest;
import com.senla.app.model.dto.response.CollectionResponse;
import com.senla.app.model.dto.response.OrderResponse;
import com.senla.app.model.entity.Order;
import com.senla.app.model.entity.sortby.OrderSortBy;
import com.senla.app.model.entity.status.OrderStatus;
import com.senla.app.service.order.OrderQueryService;
import com.senla.app.service.order.OrderService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LogManager.getLogger(OrderController.class);
    private final OrderService orderService;
    private final OrderQueryService orderQueryService;

    public OrderController(OrderService orderService, OrderQueryService orderQueryService) {
        this.orderService = orderService;
        this.orderQueryService = orderQueryService;
    }

    @PostMapping("/create")
    @LoggingOperation("создание заказа")
    public ResponseEntity<OrderResponse> createOrder(
            // @Valid - валидация
            // @RequestBody - конвертация в JSON
            @Valid @RequestBody CreateOrderRequest request
    ) {

        logger.info("Начало обработки добавления заказа");

        Order order = orderService.createOrder(
                request.getId(),
                request.getOrderedBooksNumbers(),
                request.getCustomerName()
        );

        // Хороший тон - возвращать в заголовке
        // URL созданного ресусра
        URI location = URI.create("/api/orders/" + order.getId());

        logger.info("Обработки заказа завершена");

        return ResponseEntity
                    .created(location)
                    .body(new OrderResponse(order));
    }

    @PatchMapping("/{id}/cancel")
    @LoggingOperation("отмена заказа")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Integer id
    ) {
        Order canceledOrder = orderService.cancelOrder(id);
        return ResponseEntity.ok(new OrderResponse(canceledOrder));
    }

    @PatchMapping("{id}/update/status")
    @LoggingOperation("смена статуса заказа")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable Integer id,
            @RequestParam OrderStatus newStatus
    ) {
        Order updatedOrder = orderService.changeOrderStatus(id, newStatus);
        return ResponseEntity.ok(new OrderResponse(updatedOrder));
    }

    @GetMapping({"/", ""})
    @LoggingOperation("получение отсортированных заказов")
    public ResponseEntity<CollectionResponse<OrderResponse>> getSorted(
            @RequestParam(defaultValue = "NO_SORT") OrderSortBy sort
    ) {
        List<Order> sortedOrder = orderQueryService.getSorted(sort);
        return ResponseEntity.ok(new CollectionResponse<>(sortedOrder.stream().map(OrderResponse::new).toList()));
    }

    @GetMapping("/completed")
    @LoggingOperation("получение завершенных в интервал заказов")
    public ResponseEntity<CollectionResponse<OrderResponse>> getCompletedOrdersInInterval(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        List<Order> orders = orderQueryService.getCompletedOrdersInInterval(
                from.getYear(), from.getMonthValue(), from.getDayOfMonth(),
                to.getYear(), to.getMonthValue(), to.getDayOfMonth(), true
            );
        return ResponseEntity.ok(new CollectionResponse<>(orders.stream().map(OrderResponse::new).toList()));
    }

    @GetMapping("/completed/{stat}")
    @LoggingOperation("получение статистики по заказам за интервал")
    public ResponseEntity<Long> getStatsInInterval(
            @PathVariable String stat,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        long statValue;

        switch (stat) {
            case "income" -> statValue = orderQueryService.getIncomeInInterval(
                    from.getYear(), from.getMonthValue(), from.getDayOfMonth(),
                    to.getYear(), to.getMonthValue(), to.getDayOfMonth()
            );
            case "amount" -> statValue = orderQueryService.getOrderAmountInInterval(
                    from.getYear(), from.getMonthValue(), from.getDayOfMonth(),
                    to.getYear(), to.getMonthValue(), to.getDayOfMonth()
            );
            default -> throw new ResourceNotFound("Статистики '" + stat + "' не существует");
        }
        return ResponseEntity.ok(statValue);
    }

    @GetMapping("/{id}")
    @LoggingOperation("получение подробностей заказа")
    public ResponseEntity<OrderResponse> getOrderDetails(
            @PathVariable Integer id
    ) {
        Order canceledOrder = orderQueryService.getOrderDetails(id);
        return ResponseEntity.ok(new OrderResponse(canceledOrder));
    }
}
