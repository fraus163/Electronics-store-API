package com.fraus.spring.shop.repository;

import com.fraus.spring.shop.repository.entity.BrandType;
import com.fraus.spring.shop.repository.entity.Product;
import com.fraus.spring.shop.repository.entity.ProductType;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where " +
    "(:brand is null or p.brand = :brand) and " +
    "(:type is null or p.type = :type)")
    Page<Product> findByFilter(
            @Param("brand") BrandType brand,
            @Param("type") ProductType type,
            Pageable pageable
    );
}
