package com.fraus.spring.order.service;

import com.fraus.spring.cart.repository.CartRepository;
import com.fraus.spring.cart.repository.entity.Cart;
import com.fraus.spring.order.mapper.OrderMapper;
import com.fraus.spring.order.repository.OrderRepository;
import com.fraus.spring.order.repository.entity.Order;
import com.fraus.spring.order.repository.entity.OrderStatus;
import com.fraus.spring.order.web.Dto.OrderDto;
import com.fraus.spring.shop.repository.ProductRepository;
import com.fraus.spring.shop.repository.entity.Product;
import com.fraus.spring.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
    }

    public void createOrder(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found: id=" + cartId));
        Product product = productRepository.findById(cart.getProduct().getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found: id=" + cart.getProduct().getId()));
        int remainingQuantity = product.getQuantity() - cart.getQuantity();
        if (remainingQuantity >= 0) {
            orderRepository.save(new Order(
                    null,
                    cart.getUser(),
                    cart.getProduct(),
                    cart.getQuantity(),
                    LocalDateTime.now(),
                    OrderStatus.CREATED
            ));

            productRepository.save(new Product(
                    product.getId(),
                    product.getBrand(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getType(),
                    remainingQuantity
            ));

            cartRepository.removeById(cartId);

            log.info("Order is created: cart id={}", cartId);
        } else throw new IllegalArgumentException("Product quantity of " + cart.getQuantity() + " is not available");
    }

    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: id=" + orderId));
        Product product = productRepository.findById(order.getProduct().getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found: id=" + order.getProduct().getId()));

        if (order.getStatus() == OrderStatus.CREATED) {
            orderRepository.removeById(orderId);
            productRepository.save(new Product(
                    product.getId(),
                    product.getBrand(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getType(),
                    product.getQuantity() + order.getQuantity()
            ));
        } else throw new IllegalArgumentException("Order cannot be cancelled: id=" + orderId);
    }

    public void startOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: id=" + orderId));

        if (order.getStatus() == OrderStatus.CREATED) {
            order.setStatus(OrderStatus.IN_PROGRESS);
            orderRepository.save(order);
        } else throw new IllegalArgumentException("Unable to start an order");
        log.info("Order has been start: id={}", orderId);
    }

    public void completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: id=" + orderId));

        if (order.getStatus() == OrderStatus.IN_PROGRESS) {
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);
        } else throw new IllegalArgumentException("Unable to complete an order");
        log.info("Order has been complete: id={}", orderId);
    }

    public List<OrderDto> findAllOrdersByUserId(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: id=" + userId));
        List<Order> orders = orderRepository.findAllByUser(user);

        log.info("Orders has been displayed: userId={}", userId);
        return orders.stream().map(orderMapper::toDto).toList();
    }
}
