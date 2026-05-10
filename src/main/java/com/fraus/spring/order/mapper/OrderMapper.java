package com.fraus.spring.order.mapper;

import com.fraus.spring.order.repository.entity.Order;
import com.fraus.spring.order.web.Dto.OrderDto;
import com.fraus.spring.shop.mapper.ProductMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    private final ProductMapper productMapper;

    public OrderMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public OrderDto toDto(Order order) {
        return new OrderDto(
                productMapper.toDto(order.getProduct()),
                order.getQuantity(),
                order.getCreated_at(),
                order.getStatus()
        );
    }
}
