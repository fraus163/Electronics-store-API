package com.fraus.spring.repository;

import com.fraus.spring.shop.repository.ProductRepository;
import com.fraus.spring.shop.repository.entity.BrandType;
import com.fraus.spring.shop.repository.entity.Product;
import com.fraus.spring.shop.repository.entity.ProductType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldReturnProductsByFilter() {
        Product product = new Product(
                null,
                BrandType.INTEL,
                "Core Ultra 5 245K",
                "Процессор",
                new BigDecimal("21000"),
                ProductType.CPU,
                10
        );
        BrandType brand = BrandType.INTEL;
        ProductType type = ProductType.CPU;
        Pageable pageable = PageRequest.of(0, 1);

        Product saved = productRepository.save(product);

        Page<Product> result = productRepository.findByFilter(brand, type, pageable);

        assertEquals("Core Ultra 5 245K", result.getContent().getFirst().getName());
    }
}
