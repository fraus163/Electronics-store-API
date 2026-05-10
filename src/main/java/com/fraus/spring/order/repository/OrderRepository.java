package com.fraus.spring.order.repository;

import com.fraus.spring.order.repository.entity.Order;
import com.fraus.spring.user.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    void removeById(Long id);
    List<Order> findAllByUser(User user);
}
