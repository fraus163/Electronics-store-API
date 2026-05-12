package com.fraus.spring.order.web.Dto;

import com.fraus.spring.order.repository.entity.OrderStatus;
import com.fraus.spring.shop.web.Dto.ProductDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record OrderDto (
        @NotNull
        Long productId,

        @NotNull
        @Positive
        int quantity,

        @NotNull
        LocalDateTime created_at,

        @NotNull
        OrderStatus status
){
}
