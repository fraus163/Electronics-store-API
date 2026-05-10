package com.fraus.spring.shop.web;

import com.fraus.spring.shop.repository.entity.BrandType;
import com.fraus.spring.shop.repository.entity.ProductType;
import com.fraus.spring.shop.service.ShopService;
import com.fraus.spring.shop.web.Dto.ProductDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class ShopController {
    private final Logger log = LoggerFactory.getLogger(ShopController.class);
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createProduct(
            @RequestBody @Valid ProductDto productRequest
    ) {
        log.info("Controller createProduct is called");
        shopService.createProduct(productRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> findByFilter(
            @RequestParam(required = false) BrandType brand,
            @RequestParam(required = false) ProductType type,
            Pageable pageable
    ) {
        log.info("Controller findByFilter is called");
        List<ProductDto> products = shopService.findByFilter(brand, type, pageable);

        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findProductById(@PathVariable Long id) {
        ProductDto product = shopService.findProductById(id);

        return ResponseEntity.ok().body(product);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ) {
        log.info("Controller deleteProduct is called");
        shopService.deleteProduct(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductDto productRequest
    ){
        log.info("Controller updateProduct is called");
        ProductDto product = shopService.updateProduct(id, productRequest);

        return ResponseEntity.ok().body(product);
    }
}