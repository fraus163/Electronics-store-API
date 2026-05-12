package com.fraus.spring.controller;

import com.fraus.spring.cart.service.CartService;
import com.fraus.spring.cart.web.CartController;
import com.fraus.spring.cart.web.Dto.CartDto;
import com.fraus.spring.cart.web.Dto.QuantityRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CartControllerTest {
    @MockitoBean
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateCart() throws Exception {
        CartDto cartDto = new CartDto(
                1L,
                1
        );

        mockMvc.perform(post("/api/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartDto)))
                .andExpect(status().isCreated());

        verify(cartService)
                .addToCart(any());
    }

    @Test
    void shouldDeleteFromCart() throws Exception {
        mockMvc.perform(delete("/api/cart/1"))
                .andExpect(status().isOk());

        verify(cartService)
                .deleteFromCart(1L);
    }

    @Test
    void shouldUpdateQuantity() throws Exception {
        QuantityRequest quantityRequest = new QuantityRequest(
                1
        );

        CartDto cartDto = new CartDto(
                1L,
                1
        );

        when(cartService.updateQuantity(1L, any()))
                .thenReturn(cartDto);
        mockMvc.perform(patch("/api/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quantityRequest)))
                .andExpect(status().isOk());

        verify(cartService)
                .updateQuantity(1L, any());
    }

    @Test
    void shouldReturn404WhenQuantityIsNegative() throws Exception {
        QuantityRequest quantityRequest = new QuantityRequest(
                -1
        );

        mockMvc.perform(patch("/api/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quantityRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenUserIdIsNull() throws Exception {
        CartDto cartDto = new CartDto(
                1L,
                1
        );

        mockMvc.perform(post("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnCartByUserId() throws Exception {
        CartDto cartDto = new CartDto(
                1L,
                1
        );

        List<CartDto> cart = List.of(cartDto);

        when(cartService.findAllProductsFromCart())
                .thenReturn(cart);

        mockMvc.perform(get("/api/cart/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(cartDto.productId()))
                .andExpect(jsonPath("$[0].quantity").value(cartDto.quantity()));

        verify(cartService)
                .findAllProductsFromCart();
    }
}
