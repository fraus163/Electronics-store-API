package com.fraus.spring.order.repository.entity;

import com.fraus.spring.shop.repository.entity.Product;
import com.fraus.spring.user.repository.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    @Column(name = "quantity")
    @NotNull
    private int quantity;

    @Column(name = "created_at")
    @NotNull
    private LocalDateTime created_at;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @NotNull
    private OrderStatus status;
}
