package com.fraus.spring.cart.web;

import com.fraus.spring.cart.service.CartService;
import com.fraus.spring.cart.web.Dto.CartDto;
import com.fraus.spring.cart.web.Dto.QuantityRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private static final Logger log = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<Void> addToCart(
            @RequestBody @Valid CartDto cartDto
    ) {
        log.info("Controller addToCart is called");
        cartService.addToCart(cartDto);

        return ResponseEntity
                .status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFromCart(@PathVariable Long id) {
        log.info("Controller deleteFromCart is called");
        cartService.deleteFromCart(id);

        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<CartDto> updateQuantity(@RequestBody @Valid QuantityRequest quantityRequest) {
        log.info("Controller updateQuantity is called");
        CartDto result = cartService.updateQuantity(quantityRequest);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartDto>> findAllProductsFromCart(@PathVariable Long userId) {
        log.info("Controller findAllProductsFromCart is called");
        List<CartDto> cart = cartService.findAllProductsFromCart(userId);

        return ResponseEntity.ok().body(cart);
    }

}
