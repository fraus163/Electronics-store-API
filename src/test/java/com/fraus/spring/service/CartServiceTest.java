package com.fraus.spring.service;

import com.fraus.spring.cart.mapper.CartMapper;
import com.fraus.spring.cart.repository.CartRepository;
import com.fraus.spring.cart.repository.entity.Cart;
import com.fraus.spring.cart.service.CartService;
import com.fraus.spring.cart.web.Dto.CartDto;
import com.fraus.spring.cart.web.Dto.QuantityRequest;
import com.fraus.spring.globalException.exception.InvalidUserException;
import com.fraus.spring.shop.repository.ProductRepository;
import com.fraus.spring.shop.repository.entity.Product;
import com.fraus.spring.user.repository.UserRepository;
import com.fraus.spring.user.repository.entity.Role;
import com.fraus.spring.user.repository.entity.User;
import com.fraus.spring.user.repository.entity.UserRole;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    void createSecurityContext(String username) {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    User createUser(
            Long id,
            String username,
            String email,
            String password
    ) {
        Role role = new Role(
                1,
                UserRole.USER
        );

        return new User(
                id,
                username,
                email,
                password,
                Set.of(role)
        );
    }

    @Test
    void shouldAddToCart() {
        User user = createUser(
                1L,
                "user",
                "user@gmail.com",
                "user"
        );

        createSecurityContext(user.getUsername());

        CartDto cartDto = new CartDto(
                1L,
                1
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(new Product()));
        when(userRepository.findUserByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        cartService.addToCart(cartDto);

        verify(cartRepository)
                .save(any());
    }

    @Test
    void shouldReturnThrowWhenUserNotFound() {
        User user = createUser(
                1L,
                "user",
                "user@gmail.com",
                "user"
        );

        createSecurityContext(user.getUsername());

        CartDto cartDto = new CartDto(
                1L,
                1
        );

        when(userRepository.findUserByUsername(user.getUsername()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.addToCart(cartDto));
    }

    @Test
    void shouldReturnThrowWhenProductNotFound() {
        User user = createUser(
                1L,
                "user",
                "user@gmail.com",
                "user"
        );

        createSecurityContext(user.getUsername());

        CartDto cartDto = new CartDto(
                1L,
                1
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());
        when(userRepository.findUserByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        assertThrows(EntityNotFoundException.class, () -> cartService.addToCart(cartDto));
    }

    @Test
    void shouldDeleteProductFromCart() throws InvalidUserException {
        User user = createUser(
                1L,
                "user",
                "user@gmail.com",
                "user"
        );

        createSecurityContext(user.getUsername());

        Cart cart = new Cart(
                1L,
                user,
                new Product(),
                1
        );

        when(cartRepository.findById(1L))
                .thenReturn(Optional.of(cart));
        when(userRepository.findUserByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        cartService.deleteFromCart(1L);

        verify(cartRepository)
                .removeById(1L);
    }

    @Test
    void shouldUpdateProductQuantityFromCart() throws InvalidUserException {
        User user = createUser(
                1L,
                "user",
                "user@gmail.com",
                "user"
        );

        createSecurityContext(user.getUsername());

        Cart cart = new Cart(
                1L,
                user,
                new Product(),
                1
        );

        QuantityRequest quantityRequest = new QuantityRequest(
                1
        );

        CartDto cartDto = new CartDto(
                1L,
                1
        );

        when(userRepository.findUserByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));
        when(cartRepository.findById(1L))
                .thenReturn(Optional.of(cart));
        when(cartRepository.save(any()))
                .thenReturn(cart);
        when(cartMapper.toDto(cart))
                .thenReturn(cartDto);

        CartDto result = cartService.updateQuantity(1L, quantityRequest);

        assertNotNull(result);

        verify(cartRepository)
                .save(any());
        verify(cartMapper)
                .toDto(any());
    }

    @Test
    void shouldReturnAllProductsFromCart() {
        User user = createUser(
                1L,
                "user",
                "user@gmail.com",
                "user"
        );

        createSecurityContext(user.getUsername());

        Cart cart = new Cart();

        when(userRepository.findUserByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));
        when(cartRepository.findCartByUser_Id(1L))
                .thenReturn(List.of(cart));
        when(cartMapper.toDto(cart))
                .thenReturn(new CartDto(
                        1L,
                        1
                ));

        List<CartDto> result = cartService.findAllProductsFromCart();

        assertEquals(1, result.getFirst().quantity());
    }

    @Test
    void shouldThrowWhenInvalidUserWantsUpdateQuantity() {
        User user = createUser(
                1L,
                "user",
                "user@gmail.com",
                "user"
        );

        User invalidUser = createUser(
                2L,
                "user2",
                "user2@gmail.com",
                "user2"
        );

        createSecurityContext(invalidUser.getUsername());

        Cart cart = new Cart(
                1L,
                user,
                new Product(),
                1
        );

       QuantityRequest quantityRequest = new QuantityRequest(1);

       when(userRepository.findUserByUsername(invalidUser.getUsername()))
               .thenReturn(Optional.of(invalidUser));
       when(cartRepository.findById(1L))
               .thenReturn(Optional.of(cart));

        assertThrows(InvalidUserException.class, () -> cartService.updateQuantity(1L, quantityRequest));
    }

    @Test
    void shouldThrowWhenInvalidUserWantsToDeleteFromCart() {
        User user = createUser(
                1L,
                "user",
                "user@gmail.com",
                "user"
        );

        User invalidUser = createUser(
                2L,
                "user2",
                "user2@gmail.com",
                "user2"
        );

        createSecurityContext(invalidUser.getUsername());

        Cart cart = new Cart(
                1L,
                user,
                new Product(),
                1
        );

        when(userRepository.findUserByUsername(invalidUser.getUsername()))
                .thenReturn(Optional.of(invalidUser));
        when(cartRepository.findById(1L))
                .thenReturn(Optional.of(cart));

        assertThrows(InvalidUserException.class, () -> cartService.deleteFromCart(1L));
    }
}
