package com.fraus.spring.shop.web.Dto;

import com.fraus.spring.shop.repository.entity.BrandType;
import com.fraus.spring.shop.repository.entity.ProductType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductDto (
        @NotNull
        BrandType brand,

        @NotNull
        @NotBlank
        String name,

        @NotNull
        @NotBlank
        String description,

        @NotNull
        @Positive
        BigDecimal price,

        @NotNull
        ProductType type,

        @NotNull
        @PositiveOrZero
        int quantity
){
}
