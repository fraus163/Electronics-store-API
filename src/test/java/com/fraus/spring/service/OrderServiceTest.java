package com.fraus.spring.service;

import com.fraus.spring.cart.repository.CartRepository;
import com.fraus.spring.cart.repository.entity.Cart;
import com.fraus.spring.order.mapper.OrderMapper;
import com.fraus.spring.order.repository.OrderRepository;
import com.fraus.spring.order.repository.entity.Order;
import com.fraus.spring.order.repository.entity.OrderStatus;
import com.fraus.spring.order.service.OrderService;
import com.fraus.spring.order.web.Dto.OrderDto;
import com.fraus.spring.shop.repository.ProductRepository;
import com.fraus.spring.shop.repository.entity.BrandType;
import com.fraus.spring.shop.repository.entity.Product;
import com.fraus.spring.shop.repository.entity.ProductType;
import com.fraus.spring.user.repository.UserRepository;
import com.fraus.spring.user.repository.entity.User;
import com.fraus.spring.user.repository.entity.UserRole;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCreateOrder() {
        Product product = new Product(
                1L,
                BrandType.INTEL,
                "Core Ultra 5 245K",
                "Процессор",
                new BigDecimal("21000"),
                ProductType.CPU,
                10
        );

        Cart cart = new Cart(
                1L,
                new User(),
                product,
                1
        );


        when(cartRepository.findById(1L))
                .thenReturn(Optional.of(cart));
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        orderService.createOrder(1L);

        verify(orderRepository)
                .save(any());
        verify(orderRepository)
                .save(any());
        verify(productRepository)
                .save(any());
        verify(cartRepository)
                .removeById(1L);
    }

    @Test
    void shouldDeleteOrder() {
        Product product = new Product(
                1L,
                BrandType.INTEL,
                "Core Ultra 5 245K",
                "Процессор",
                new BigDecimal("21000"),
                ProductType.CPU,
                10
        );

        Order order = new Order(
                1L,
                new User(),
                product,
                1,
                LocalDateTime.now(),
                OrderStatus.CREATED
        );

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));
        when(productRepository.findById(product.getId()))
                .thenReturn(Optional.of(product));

        orderService.deleteOrder(1L);

        verify(orderRepository)
                .removeById(1L);
        verify(productRepository)
                .save(any());
    }

    @Test
    void shouldStartOrder() {
        Order order = new Order(
                1L,
                new User(),
                new Product(),
                1,
                LocalDateTime.now(),
                OrderStatus.CREATED
        );

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        orderService.startOrder(1L);

        verify(orderRepository)
                .save(any());
    }

    @Test
    void shouldCompleteOrder() {
        Order order = new Order(
                1L,
                new User(),
                new Product(),
                1,
                LocalDateTime.now(),
                OrderStatus.IN_PROGRESS
        );

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        orderService.completeOrder(1L);

        verify(orderRepository)
                .save(any());
    }

    @Test
    void shouldFindAllOrdersByUserId() {
        User user = new User();

        Order order = new Order(
                1L,
                user,
                new Product(),
                1,
                LocalDateTime.now(),
                OrderStatus.CREATED
        );

        OrderDto orderDto = new OrderDto(
                null,
                order.getQuantity(),
                order.getCreated_at(),
                order.getStatus()
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(orderRepository.findAllByUser(user))
                .thenReturn(List.of(order));
        when(orderMapper.toDto(order))
                .thenReturn(orderDto);

        List<OrderDto> result = orderService.findAllOrdersByUserId(1L);

        assertEquals(1, result.getFirst().quantity());
    }

    @Test
    void shouldThrowWhenOrderNotFound() {
        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> orderService.startOrder(1L));
    }

    @Test
    void shouldThrowWhenUnableToStartOrder() {
        Order order = new Order(
                1L,
                new User(),
                new Product(),
                1,
                LocalDateTime.now(),
                OrderStatus.COMPLETED
        );

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(IllegalArgumentException.class, () -> orderService.startOrder(1L));
    }

    @Test
    void shouldThrowWhenUnableToCompleteOrder() {
        Order order = new Order(
                1L,
                new User(),
                new Product(),
                1,
                LocalDateTime.now(),
                OrderStatus.CREATED
        );

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(IllegalArgumentException.class, () -> orderService.completeOrder(1L));
    }
}
