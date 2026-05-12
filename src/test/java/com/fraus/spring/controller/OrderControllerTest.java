package com.fraus.spring.controller;

import com.fraus.spring.order.repository.entity.OrderStatus;
import com.fraus.spring.order.service.OrderService;
import com.fraus.spring.order.web.Dto.OrderDto;
import com.fraus.spring.order.web.OrderController;
import com.fraus.spring.shop.repository.entity.BrandType;
import com.fraus.spring.shop.repository.entity.Product;
import com.fraus.spring.shop.repository.entity.ProductType;
import com.fraus.spring.shop.web.Dto.ProductDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {
    @MockitoBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllOrdersByUser() throws Exception {
        LocalDateTime createdAt = LocalDateTime.now();
        String formattedDate = createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        OrderDto orderDto = new OrderDto(
                1L,
                1,
                createdAt,
                OrderStatus.CREATED
        );

        when(orderService.findAllOrdersByUser())
                .thenReturn(List.of(orderDto));

        mockMvc.perform(get("/api/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].quantity").value(1))
                .andExpect(jsonPath("$[0].created_at").value(formattedDate))
                .andExpect(jsonPath("$[0].status").value(OrderStatus.CREATED.toString()));
    }

    @Test
    void shouldCreateOrder() throws Exception {
        doNothing().when(orderService)
                .createOrder(1L);

        mockMvc.perform(post("/api/order/1"))
                .andExpect(status().isCreated());

        verify(orderService)
                .createOrder(1L);
    }

    @Test
    void shouldDeleteOrder() throws Exception {
        doNothing().when(orderService)
                .deleteOrder(1L);

        mockMvc.perform(delete("/api/order/1"))
                .andExpect(status().isOk());

        verify(orderService)
                .deleteOrder(1L);
    }

    @Test
    void shouldStartOrder() throws Exception {
        doNothing().when(orderService)
                .startOrder(1L);

        mockMvc.perform(post("/api/order/start/1"))
                .andExpect(status().isOk());

        verify(orderService)
                .startOrder(1L);
    }

    @Test
    void shouldCompleteOrder() throws Exception {
        doNothing().when(orderService)
                .completeOrder(1L);

        mockMvc.perform(post("/api/order/complete/1"))
                .andExpect(status().isOk());

        verify(orderService)
                .completeOrder(1L);
    }

    @Test
    void shouldReturn404WhenOrderNotFound() throws Exception {
        doThrow(new EntityNotFoundException()).when(orderService)
                .startOrder(1L);

        mockMvc.perform(post("/api/order/start/1"))
                .andExpect(status().isNotFound());
    }
}
