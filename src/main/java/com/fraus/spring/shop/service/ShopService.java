package com.fraus.spring.shop.service;

import com.fraus.spring.shop.mapper.ProductMapper;
import com.fraus.spring.shop.repository.ProductRepository;
import com.fraus.spring.shop.repository.entity.BrandType;
import com.fraus.spring.shop.repository.entity.Product;
import com.fraus.spring.shop.repository.entity.ProductType;
import com.fraus.spring.shop.web.Dto.ProductDto;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShopService {
    private final Logger log = LoggerFactory.getLogger(ShopService.class);
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ShopService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public void createProduct(ProductDto product) {
        productRepository.save(new Product(
                null,
                product.brand(),
                product.name(),
                product.description(),
                product.price(),
                product.type(),
                product.quantity()
        ));
        log.info("Entry was added successfully");
    }

    public List<ProductDto> findByFilter(
            BrandType brand,
            ProductType type,
            Pageable pageable
    ) {
        log.info("Entries are displayed");
        return productRepository.findByFilter(brand, type, pageable)
                .map(productMapper::toDto).toList();
    }

    public ProductDto findProductById(Long id) {
        var findedProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found element by id=" + id));
        log.info("Entry is displayed: id={}", id);
        return productMapper.toDto(findedProduct);
    }

    public void deleteProduct(Long id) {
        if(!productRepository.existsById(id))
            throw new EntityNotFoundException("Not found element by id=" + id);
        productRepository.deleteById(id);
        log.info("Entry is deleted: id={}", id);
    }

    public ProductDto updateProduct(Long id, ProductDto product) {
//        if(!productRepository.existsById(id))
//            throw new EntityNotFoundException("Not found element by id=" + id);
        Product updatedProduct = productRepository.save(new Product(
                id,
                product.brand(),
                product.description(),
                product.name(),
                product.price(),
                product.type(),
                product.quantity()
        ));
        log.info("Entry is updated: id={}", id);
        return productMapper.toDto(updatedProduct);
    }
}