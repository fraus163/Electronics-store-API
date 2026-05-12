package com.fraus.spring.cart.repository;

import com.fraus.spring.cart.repository.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    void removeById(Long id);
    List<Cart> findCartByUser_Id(Long userId);
}
