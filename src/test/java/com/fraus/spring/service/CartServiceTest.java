package com.fraus.spring.service;

import com.fraus.spring.cart.mapper.CartMapper;
import com.fraus.spring.cart.repository.CartRepository;
import com.fraus.spring.cart.repository.entity.Cart;
import com.fraus.spring.cart.service.CartService;
import com.fraus.spring.cart.web.Dto.CartDto;
import com.fraus.spring.cart.web.Dto.QuantityRequest;
import com.fraus.spring.shop.repository.ProductRepository;
import com.fraus.spring.shop.repository.entity.Product;
import com.fraus.spring.user.repository.UserRepository;
import com.fraus.spring.user.repository.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    CartMapper cartMapper;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void shouldCreateCart() {
        CartDto cartDto = new CartDto(
                1L,
                1L,
                10
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(new Product()));

        cartService.addToCart(cartDto);

        verify(cartRepository)
                .save(any());
    }

    @Test
    void shouldReturnThrowWhenUserNotFound() {
        CartDto cartDto = new CartDto(
                1L,
                1L,
                10
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.addToCart(cartDto));
    }

    @Test
    void shouldReturnThrowWhenProductNotFound() {
        CartDto cartDto = new CartDto(
                1L,
                1L,
                10
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));

        assertThrows(EntityNotFoundException.class, () -> cartService.addToCart(cartDto));
    }

    @Test
    void shouldDeleteProductFromCart() {
        when(cartRepository.existsById(1L))
                .thenReturn(true);

        cartService.deleteFromCart(1L);

        verify(cartRepository)
                .removeById(1L);
    }

    @Test
    void shouldUpdateProductQuantityFromCart() {
        QuantityRequest quantityRequest = new QuantityRequest(
                1L,
                1
        );

        CartDto cartDto = new CartDto(
                1L,
                1L,
                10
        );

        Cart cart = new Cart();

        when(cartRepository.findById(1L))
                .thenReturn(Optional.of(cart));
        when(cartRepository.save(any()))
                .thenReturn(cart);
        when(cartMapper.toDto(cart))
                .thenReturn(cartDto);

        CartDto result = cartService.updateQuantity(quantityRequest);

        assertNotNull(result);

        verify(cartMapper)
                .toDto(cart);
        verify(cartRepository)
                .findById(1L);
        verify(cartRepository)
                .save(any());
    }

    @Test
    void shouldReturnAllProductsFromCart() {
        Cart cart = new Cart();

        when(cartRepository.findCartByUser_Id(1L))
                .thenReturn(Optional.of(List.of(cart)));
        when(cartMapper.toDto(cart))
                .thenReturn(new CartDto(
                        1L,
                        1L,
                        1
                ));

        List<CartDto> result = cartService.findAllProductsFromCart(1L);

        assertEquals(1L, result.getFirst().quantity());

        verify(cartRepository)
                .findCartByUser_Id(1L);
        verify(cartMapper)
                .toDto(cart);
    }
}
