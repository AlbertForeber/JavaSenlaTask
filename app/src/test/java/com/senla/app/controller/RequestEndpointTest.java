package com.senla.app.controller;

import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.Request;
import com.senla.app.model.entity.status.BookStatus;
import com.senla.app.service.config.TestSecurityConfig;
import com.senla.app.service.request.RequestQueryService;
import com.senla.app.service.request.RequestService;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Тест эндпоинтов Request")
@ContextConfiguration(classes = {
        RequestController.class,
        TestSecurityConfig.class,
        RequestEndpointTest.TestConfig.class
})
@WebAppConfiguration
public class RequestEndpointTest {

    @Autowired
    private RequestQueryService requestQueryServiceMock;

    @Autowired
    private RequestService requestServiceMock;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        Mockito.reset(requestServiceMock, requestQueryServiceMock);
    }

    @Configuration
    static class TestConfig {

        @Bean
        @Primary
        public RequestQueryService requestQueryService() {
            return Mockito.mock(RequestQueryService.class);
        }

        @Bean
        @Primary
        public RequestService requestService() {
            return Mockito.mock(RequestService.class);
        }
    }

    @Test
    @Tag("integration")
    @DisplayName("/api/requests без прав должно отклоняться")
    @WithAnonymousUser
    public void viewAllRequestsIsNotAccessibleByAnonymous() throws Exception {
        mockMvc.perform(get("/api/requests/"))
                .andExpect(status().isForbidden());
        verifyNoInteractions(requestQueryServiceMock);
    }

    @Test
    @Tag("integration")
    @DisplayName("/api/requests c SCOPE request:view_all должно возвращать запросы")
    @WithMockUser(authorities = "SCOPE_request:view_all")
    public void viewAllRequestsIsAccessibleAndShouldReturnWithScope() throws Exception {
        when(requestQueryServiceMock.getSorted(any())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/requests/"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"objects\":[]}"));
    }

    @Test
    @Tag("integration")
    @DisplayName("/api/requests/add без прав должно отклоняться")
    @WithAnonymousUser
    public void addRequestsIsNotAccessibleByAnonymous() throws Exception {
        mockMvc.perform(post("/api/requests/add?bookId=1"))
                .andExpect(status().isForbidden());
        verifyNoInteractions(requestServiceMock);
    }

    @Test
    @Tag("integration")
    @DisplayName("/api/add с SCOPE request:add должно возвращать созданный запрос")
    @WithMockUser(authorities = "SCOPE_request:add")
    public void addRequestsIsAccessibleAndShouldReturn() throws Exception {
        Request request = new Request(1, new Book(
                1,
                "",
                "",
                new GregorianCalendar(1, Calendar.JANUARY, 1),
                new GregorianCalendar(2, Calendar.JANUARY, 1),
                0,
                BookStatus.FREE,
                null
        ));
        when(requestServiceMock.createRequest(anyInt())).thenReturn(request);

        mockMvc.perform(post("/api/requests/add?bookId=1"))
                .andExpect(status().isCreated())
                // Умеет проверять только часть JSON
                .andExpect(content().json("{'id': 1, 'bookName': \"\", 'amount': 1}"));
    }
}
