package com.fraus.spring.shop.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "brand")
    @NotNull
    private BrandType brand;

    @Column(name = "name", length = 30, unique = true)
    @NotNull
    @NotBlank
    private String name;

    @Column(name = "description")
    @NotNull
    @NotBlank
    private String description;

    @Column(name = "price")
    @NotNull
    @Positive
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @NotNull
    private ProductType type;

    @Column(name = "quantity")
    @NotNull
    @PositiveOrZero
    private int quantity;
}
