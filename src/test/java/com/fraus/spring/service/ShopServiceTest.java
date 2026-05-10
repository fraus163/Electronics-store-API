package com.fraus.spring.service;

import com.fraus.spring.shop.mapper.ProductMapper;
import com.fraus.spring.shop.repository.ProductRepository;
import com.fraus.spring.shop.repository.entity.BrandType;
import com.fraus.spring.shop.repository.entity.Product;
import com.fraus.spring.shop.repository.entity.ProductType;
import com.fraus.spring.shop.service.ShopService;
import com.fraus.spring.shop.web.Dto.ProductDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShopServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ShopService shopService;

    @Mock
    private ProductMapper productMapper;

    @Test
    void shouldCreateProduct() {
        ProductDto productDto = new ProductDto(
                BrandType.INTEL,
                "Core Ultra 5 245K",
                "Процессор",
                new BigDecimal("21000"),
                ProductType.CPU,
                10
        );

        shopService.createProduct(productDto);

        verify(productRepository).save(any());
    }

    @Test
    void shouldReturnProductsByFilter() {
        Product product = new Product(
                1L,
                BrandType.INTEL,
                "Core Ultra 5 245K",
                "Процессор",
                new BigDecimal("21000"),
                ProductType.CPU,
                10
        );

        ProductDto productDto = new ProductDto(
                product.getBrand(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getType(),
                product.getQuantity()
        );

        BrandType brand = BrandType.INTEL;
        ProductType type = ProductType.CPU;
        Pageable pageable = PageRequest.of(0, 1);

        when(productRepository.findByFilter(brand, type, pageable))
                .thenReturn(new PageImpl<>(List.of(product)));
        when(productMapper.toDto(product))
                .thenReturn(productDto);

        List<ProductDto> products = shopService.findByFilter(brand, type, pageable);

        assertEquals("Core Ultra 5 245K", products.getFirst().name());

        verify(productRepository)
                .findByFilter(brand, type, pageable);
        verify(productMapper)
                .toDto(product);
    }

    @Test
    void shouldReturnProductById() {
        Product product = new Product(
                1L,
                BrandType.INTEL,
                "Core Ultra 5 245K",
                "Процессор",
                new BigDecimal("21000"),
                ProductType.CPU,
                10
        );

        ProductDto productDto = new ProductDto(
                product.getBrand(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getType(),
                product.getQuantity()
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(productMapper.toDto(product))
                .thenReturn(productDto);

        ProductDto result = shopService.findProductById(1L);

        assertEquals("Core Ultra 5 245K", result.name());

        verify(productRepository)
                .findById(1L);
        verify(productMapper)
                .toDto(product);
    }

    @Test
    void shouldUpdateProduct() {
        Product product = new Product(
                1L,
                BrandType.INTEL,
                "Core Ultra 5 245K",
                "Процессор",
                new BigDecimal("21000"),
                ProductType.CPU,
                10
        );

        ProductDto productDto = new ProductDto(
                product.getBrand(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getType(),
                product.getQuantity()
        );

        when(productRepository.save(any()))
                .thenReturn(product);
        when(productMapper.toDto(product))
                .thenReturn(productDto);

        ProductDto result = shopService.updateProduct(1L, productDto);

        assertNotNull(result);

        verify(productMapper)
                .toDto(product);
        verify(productRepository)
                .save(any());
    }

    @Test
    void shouldDeleteProduct() {
        when(productRepository.existsById(1L))
                .thenReturn(true);

        shopService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
        verify(productRepository).existsById(1L);
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> shopService.findProductById(1L));
    }
}
