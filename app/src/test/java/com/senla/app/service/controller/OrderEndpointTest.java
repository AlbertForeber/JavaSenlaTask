package com.senla.app.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.app.controller.OrderController;
import com.senla.app.model.dto.request.CreateOrderRequest;
import com.senla.app.model.entity.Order;
import com.senla.app.model.entity.status.OrderStatus;
import com.senla.app.service.config.TestSecurityConfig;
import com.senla.app.service.order.OrderQueryService;
import com.senla.app.service.order.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@DisplayName("Тест эндпоинтов Order")
@ContextConfiguration(classes = {
        OrderController.class,
        TestSecurityConfig.class,
        OrderEndpointTest.TestConfig.class
})
@WebAppConfiguration
public class OrderEndpointTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private OrderQueryService orderQueryServiceMock;

    @Autowired
    private OrderService orderServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        Mockito.reset(orderQueryServiceMock, orderServiceMock);
    }

    @Configuration
    static class TestConfig {

        @Bean
        @Primary
        public OrderQueryService orderQueryService() {
            return Mockito.mock(OrderQueryService.class);
        }

        @Bean
        @Primary
        public OrderService orderService() {
            return Mockito.mock(OrderService.class);
        }

        @Bean
        @Primary
        public ObjectMapper mapper() {
            return new ObjectMapper();
        }
    }

    @Test
    @Tag("integrated")
    @DisplayName("/api/orders/create без прав должен отклоняться")
    @WithAnonymousUser
    public void createOrderIsNotAccessibleByAnonymous() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();

        mockMvc.perform(post("/api/orders/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isForbidden());

        verifyNoInteractions(orderServiceMock);
    }

    @Test
    @Tag("integrated")
    @DisplayName("/api/orders/create c неверной формой отклоняться")
    @WithMockUser(authorities = "SCOPE_order:create")
    public void createOrderDeniedWhenRequestNotValid() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();

        mockMvc.perform(post("/api/orders/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(orderServiceMock);
    }

    @Test
    @Tag("integrated")
    @DisplayName("/api/orders/create должен возвращать созданный заказ, если форма верна и SCOPE order:create")
    @WithMockUser(authorities = "SCOPE_order:create")
    public void createOrderShouldReturnWhenValid() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setId(1);
        request.setOrderedBookNumbers(List.of(1));
        request.setCustomerName("customer");

        when(orderServiceMock.createOrder(anyInt(), any(), any())).thenReturn(new Order(
                1,
                Collections.emptyList(),
                0,
                ""
        ));

        mockMvc.perform(post("/api/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json("{'id': 1}"));
    }

    @Test
    @Tag("integrated")
    @DisplayName("/api/orders/{id}/cancel без прав должен отклоняться")
    @WithAnonymousUser
    public void cancelIsNotAccessibleByAnonymous() throws Exception {
        mockMvc.perform(patch("/api/orders/{id}/cancel", 1))
                .andExpect(status().isForbidden());

        verifyNoInteractions(orderServiceMock);
    }

    @Test
    @Tag("integrated")
    @DisplayName("/api/orders/{id}/cancel с SCOPE order:cancel должен возвращать отмененный заказ")
    @WithMockUser(authorities = "SCOPE_order:cancel")
    public void cancelIsAccessibleAndShouldReturnWithScope() throws Exception {
        Order order = new Order(
                1,
                Collections.emptyList(),
                0,
                ""
        );

        when(orderServiceMock.cancelOrder(anyInt())).thenReturn(order);
        mockMvc.perform(patch("/api/orders/{id}/cancel", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':  1}"));
    }

    @Test
    @Tag("integrated")
    @DisplayName("/api/orders/{id}/update/status без прав должен отклоняться")
    @WithAnonymousUser
    public void updateIsNotAccessibleByAnonymous() throws Exception {
        mockMvc.perform(patch("/api/orders/{id}/update/status", 1))
                .andExpect(status().isForbidden());

        verifyNoInteractions(orderServiceMock);
    }

    @Test
    @Tag("integrated")
    @DisplayName("/api/orders/{id}/update/status с SCOPE order:change_status должен возвращать списанную книгу")
    @WithMockUser(authorities = "SCOPE_order:change_status")
    public void updateIsAccessibleAndShouldReturnWithScope() throws Exception {
        Order order = new Order(
                1,
                Collections.emptyList(),
                0,
                ""
        );
        order.setStatus(OrderStatus.CANCELED);

        when(orderServiceMock.changeOrderStatus(anyInt(), any(OrderStatus.class))).thenReturn(order);
        mockMvc.perform(patch("/api/orders/{id}/update/status?newStatus=CANCELED", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':  1, 'status':  \"CANCELED\"}"));
    }

    @Test
    @Tag("integration")
    @DisplayName("/api/orders без прав должно отклоняться")
    @WithAnonymousUser
    public void viewAllOrdersIsNotAccessibleByAnonymous() throws Exception {
        mockMvc.perform(get("/api/orders/"))
                .andExpect(status().isForbidden());
        verifyNoInteractions(orderQueryServiceMock);
    }

    @Test
    @Tag("integration")
    @DisplayName("/api/orders c SCOPE order:view_all должно возвращать все заказы")
    @WithMockUser(authorities = "SCOPE_order:view_all")
    public void viewAllOrdersIsAccessibleAndShouldReturnWithScope() throws Exception {
        when(orderQueryServiceMock.getSorted(any())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/orders/"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"objects\":[]}"));
    }

    @Test
    @Tag("integration")
    @DisplayName("/api/orders/completed без прав должно отклоняться")
    @WithAnonymousUser
    public void viewCompletedIsNotAccessibleByAnonymous() throws Exception {
        mockMvc.perform(get("/api/orders/completed"))
                .andExpect(status().isForbidden());
        verifyNoInteractions(orderQueryServiceMock);
    }

    @Test
    @Tag("integration")
    @DisplayName("/api/orders/completed c SCOPE order:view_completed должно возвращать выполненные заказы")
    @WithMockUser(authorities = "SCOPE_order:view_completed")
    public void viewCompletedIsAccessibleAndShouldReturnWithScope() throws Exception {
        when(orderQueryServiceMock.getCompletedOrdersInInterval(
                anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyBoolean()
        )).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/orders/completed?from=2025-12-10&to=2026-01-20"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"objects\":[]}"));
    }

    @Test
    @Tag("integration")
    @DisplayName("/api/orders/completed/{stat} без прав должно отклоняться")
    @WithAnonymousUser
    public void getStatsIsNotAccessibleByAnonymous() throws Exception {
        mockMvc.perform(get("/api/orders/completed/{stat}", "income"))
                .andExpect(status().isForbidden());
        verifyNoInteractions(orderQueryServiceMock);
    }

    @Test
    @Tag("integration")
    @DisplayName("/api/orders/completed/{stat} c SCOPE order:stats и stat=income должно возвращать доход за интервал")
    @WithMockUser(authorities = "SCOPE_order:stats")
    public void getStatIncomeIsAccessibleAndShouldReturnWithScope() throws Exception {
        when(orderQueryServiceMock.getIncomeInInterval(
                anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt()
        )).thenReturn(0L);
        mockMvc.perform(get("/api/orders/completed/{stat}?from=2025-12-10&to=2026-01-20", "income"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    @Tag("integration")
    @DisplayName("/api/orders/completed/{stat} c SCOPE order:stats и stat=amount должно возвращать количество выполненных заказов за интервал")
    @WithMockUser(authorities = "SCOPE_order:stats")
    public void getStatAmountIsAccessibleAndShouldReturnWithScope() throws Exception {
        when(orderQueryServiceMock.getOrderAmountInInterval(
                anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt()
        )).thenReturn(0);
        mockMvc.perform(get("/api/orders/completed/{stat}?from=2025-12-10&to=2026-01-20", "amount"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }
}
