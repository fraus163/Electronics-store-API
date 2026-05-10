package com.fraus.spring.cart.web.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CartDto(

        @NotNull
        Long userId,

        @NotNull
        Long productId,

        @NotNull
        @PositiveOrZero
        int quantity
) {
}
