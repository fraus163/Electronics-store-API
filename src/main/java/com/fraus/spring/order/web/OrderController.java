package com.fraus.spring.order.web;

import com.fraus.spring.globalException.exception.InvalidUserException;
import com.fraus.spring.order.service.OrderService;
import com.fraus.spring.order.web.Dto.OrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> findAllOrdersByUser() {
        log.info("Controller findAllOrders is called");

        return ResponseEntity
                .ok().body(orderService.findAllOrdersByUser());
    }

    @PostMapping("/{cartId}")
    public ResponseEntity<Void> createOrder(@PathVariable Long cartId) throws InvalidUserException {
        log.info("Controller createOrder is called");
        orderService.createOrder(cartId);

        return ResponseEntity
                .status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) throws InvalidUserException {
        log.info("Controller deleteOrder is called");
        orderService.deleteOrder(orderId);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/start/{orderId}")
    public ResponseEntity<Void> startOrder(@PathVariable Long orderId) throws InvalidUserException {
        log.info("Controller startOrder is called");
        orderService.startOrder(orderId);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/complete/{orderId}")
    public ResponseEntity<Void> completeOrder(@PathVariable Long orderId) {
        log.info("Controller completeOrder is called");
        orderService.completeOrder(orderId);

        return ResponseEntity.ok().build();
    }
}
