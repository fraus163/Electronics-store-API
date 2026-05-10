package com.fraus.spring.cart.web.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record QuantityRequest (
        @NotNull
        Long id,

        @NotNull
        @PositiveOrZero
        int quantity
){
}
