package com.fraus.spring.cart.mapper;

import com.fraus.spring.cart.repository.entity.Cart;
import com.fraus.spring.cart.web.Dto.CartDto;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {
    public CartDto toDto(Cart cart) {
        return new CartDto(
                cart.getProduct().getId(),
                cart.getQuantity()
        );
    }
}
