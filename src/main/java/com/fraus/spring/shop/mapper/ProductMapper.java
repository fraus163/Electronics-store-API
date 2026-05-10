package com.fraus.spring.shop.mapper;

import com.fraus.spring.shop.repository.entity.Product;
import com.fraus.spring.shop.web.Dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toEntity(ProductDto product) {
        return new Product(
                null,
                product.brand(),
                product.name(),
                product.description(),
                product.price(),
                product.type(),
                product.quantity()
        );
    }

    public ProductDto toDto(Product product) {
        return new ProductDto(
                product.getBrand(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getType(),
                product.getQuantity()
        );
    }
}
