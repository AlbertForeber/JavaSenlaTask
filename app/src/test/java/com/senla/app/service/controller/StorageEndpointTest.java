package com.senla.app.service.controller;

import com.senla.app.controller.StorageController;
import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.status.BookStatus;
import com.senla.app.service.config.TestSecurityConfig;
import com.senla.app.service.storage.StorageQueryService;
import com.senla.app.service.storage.StorageService;
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

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Тест эндпоинтов Storage")
@ContextConfiguration(classes = {
        StorageController.class,
        TestSecurityConfig.class,
        StorageEndpointTest.TestConfig.class
})
@WebAppConfiguration
public class StorageEndpointTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private StorageQueryService storageQueryServiceMock;

    @Autowired
    private StorageService storageServiceMock;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // Без сброса могут быть проблемы с verifyNoInteractions
        // Т.к. Spring-бины не сбрасываются между тестами
        Mockito.reset(storageQueryServiceMock, storageServiceMock);
    }

    @Configuration
    static class TestConfig {

        @Bean
        @Primary
        public StorageQueryService storageQueryService() {
            return Mockito.mock(StorageQueryService.class);
        }

        @Bean
        @Primary
        public StorageService storageService() {
            return Mockito.mock(StorageService.class);
        }
    }

    @Test
    @Tag("integrated")
    @DisplayName("/api/books/{id}/write-off без прав должен отклоняться")
    @WithAnonymousUser
    public void writeOffIsNotAccessibleByAnonymous() throws Exception {
        mockMvc.perform(patch("/api/books/{id}/write-off", 1))
                .andExpect(status().isForbidden());

        verifyNoInteractions(storageServiceMock);
    }

    @Test
    @Tag("integrated")
    @DisplayName("/api/books/{id}/write-off с SCOPE book:write_off должен возвращать списанную книгу")
    @WithMockUser(authorities = "SCOPE_book:write_off")
    public void writeOffIsAccessibleAndShouldReturnWithScope() throws Exception {
        Book book = new Book(
                1,
                "",
                "",
                new GregorianCalendar(1, Calendar.JANUARY, 1),
                new GregorianCalendar(2, Calendar.JANUARY, 1),
                0,
                BookStatus.SOLD_OUT,
                null
        );

        when(storageServiceMock.writeOffBook(anyInt())).thenReturn(book);
        mockMvc.perform(patch("/api/books/{id}/write-off", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("{'articleNumber':  1, 'status':  \"SOLD_OUT\"}"));
    }

    @Test
    @Tag("integrated")
    @DisplayName("/api/books/{id}/add без прав должен отклоняться")
    @WithAnonymousUser
    public void addBookIsNotAccessibleByAnonymous() throws Exception {
        mockMvc.perform(patch("/api/books/{id}/add", 1))
                .andExpect(status().isForbidden());

        verifyNoInteractions(storageServiceMock);
    }

    @Test
    @Tag("integrated")
    @DisplayName("/api/books/{id}/write-off с SCOPE book:add должен возвращать списанную книгу")
    @WithMockUser(authorities = "SCOPE_book:add")
    public void addBookIsAccessibleAndShouldReturnWithScope() throws Exception {
        Book book = new Book(
                1,
                "",
                "",
                new GregorianCalendar(1, Calendar.JANUARY, 1),
                new GregorianCalendar(2, Calendar.JANUARY, 1),
                0,
                BookStatus.FREE,
                null
        );

        when(storageServiceMock.addBookToStorage(anyInt())).thenReturn(book);
        mockMvc.perform(patch("/api/books/{id}/add", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("{'articleNumber':  1, 'status':  \"FREE\"}"));
    }

    @Test
    @Tag("integration")
    @DisplayName("/api/books без прав должно отклоняться")
    @WithAnonymousUser
    public void viewAllBooksIsNotAccessibleByAnonymous() throws Exception {
        mockMvc.perform(get("/api/books/"))
                .andExpect(status().isForbidden());
        verifyNoInteractions(storageQueryServiceMock);
    }

    @Test
    @Tag("integration")
    @DisplayName("/api/books c SCOPE book:view_all должно возвращать все книги")
    @WithMockUser(authorities = "SCOPE_book:view_all")
    public void viewAllBooksIsAccessibleAndShouldReturnWithScope() throws Exception {
        when(storageQueryServiceMock.getSorted(any())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/books/"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"objects\":[]}"));
    }

    @Test
    @Tag("integration")
    @DisplayName("/api/books/hard-to-sell без прав должно отклоняться")
    @WithAnonymousUser
    public void viewDeadStockIsNotAccessibleByAnonymous() throws Exception {
        mockMvc.perform(get("/api/books/hard-to-sell?now=2025-10-12"))
                .andExpect(status().isForbidden());
        verifyNoInteractions(storageQueryServiceMock);
    }

    @Test
    @Tag("integration")
    @DisplayName("/api/books/hard-to-sell c SCOPE book:dead_stock должно возвращать малопродаваемые книги")
    @WithMockUser(authorities = "SCOPE_book:dead_stock")
    public void viewDeadStockRequestsIsAccessibleAndShouldReturnWithScope() throws Exception {
        when(storageQueryServiceMock.getHardToSell(anyInt(), anyInt(), anyInt())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/books/hard-to-sell?now=2025-10-12"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"objects\":[]}"));
    }

    @Test
    @Tag("integrated")
    @DisplayName("/api/books/{id}/description без прав должен отклоняться")
    @WithAnonymousUser
    public void getDescriptionIsNotAccessibleByAnonymous() throws Exception {
        mockMvc.perform(get("/api/books/{id}/description", 1))
                .andExpect(status().isForbidden());

        verifyNoInteractions(storageServiceMock);
    }

    @Test
    @Tag("integrated")
    @DisplayName("/api/books/{id}/description с SCOPE book:view_description должен возвращать описание книги")
    @WithMockUser(authorities = "SCOPE_book:view_description")
    public void getDescriptionIsAccessibleAndShouldReturnWithScope() throws Exception {
        Book book = new Book(
                1,
                "",
                "test desc",
                new GregorianCalendar(1, Calendar.JANUARY, 1),
                new GregorianCalendar(2, Calendar.JANUARY, 1),
                0,
                BookStatus.FREE,
                null
        );

        when(storageQueryServiceMock.getBookDescription(anyInt())).thenReturn(book.getDescription());
        mockMvc.perform(get("/api/books/{id}/description", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("test desc"));
    }
}
