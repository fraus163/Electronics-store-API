package com.fraus.spring.cart.service;

import com.fraus.spring.cart.mapper.CartMapper;
import com.fraus.spring.cart.repository.CartRepository;
import com.fraus.spring.cart.repository.entity.Cart;
import com.fraus.spring.cart.web.Dto.CartDto;
import com.fraus.spring.cart.web.Dto.QuantityRequest;
import com.fraus.spring.shop.repository.ProductRepository;
import com.fraus.spring.shop.repository.entity.Product;
import com.fraus.spring.user.repository.UserRepository;
import com.fraus.spring.user.repository.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartService {
    private static final Logger log = LoggerFactory.getLogger(CartService.class);
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
    }

    public void addToCart(CartDto cartDto) {
        User user = userRepository.findById(cartDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: id=" + cartDto.userId()));

        Product product = productRepository.findById(cartDto.productId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found: id=" + cartDto.productId()));

        cartRepository.save(new Cart(
                null,
                user,
                product,
                cartDto.quantity()
        ));
        log.info("User id={} added product id={} in quantity={}", cartDto.userId(), cartDto.productId(), cartDto.quantity());
    }

    public void deleteFromCart(Long id) {
        if (cartRepository.existsById(id))
            cartRepository.removeById(id);
        else throw new EntityNotFoundException("Cart not found: id=" + id);
        log.info("Cart id={} has been deleted", id);
    }

    public CartDto updateQuantity(QuantityRequest quantityRequest) {
        Cart cart = cartRepository.findById(quantityRequest.id())
                .orElseThrow(() -> new EntityNotFoundException("Cart not found: id=" + quantityRequest.id()));
        Cart result = cartRepository.save(new Cart(
                cart.getId(),
                cart.getUser(),
                cart.getProduct(),
                quantityRequest.quantity()
        ));

        log.info("Cart id={} has been updated: new quantity={}", quantityRequest.id(), quantityRequest.quantity());
        return cartMapper.toDto(result);
    }

    public List<CartDto> findAllProductsFromCart(Long userId) {
        var cart = cartRepository.findCartByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found: user id=" + userId));

        log.info("Cart are displayed: userId={}", userId);
        return cart.stream().map(cartMapper::toDto).toList();
    }
}
