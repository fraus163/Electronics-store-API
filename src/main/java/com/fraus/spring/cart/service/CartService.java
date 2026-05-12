package com.fraus.spring.cart.service;

import com.fraus.spring.cart.mapper.CartMapper;
import com.fraus.spring.cart.repository.CartRepository;
import com.fraus.spring.cart.repository.entity.Cart;
import com.fraus.spring.cart.web.Dto.CartDto;
import com.fraus.spring.cart.web.Dto.QuantityRequest;
import com.fraus.spring.globalException.exception.InvalidUserException;
import com.fraus.spring.shop.repository.ProductRepository;
import com.fraus.spring.shop.repository.entity.Product;
import com.fraus.spring.user.repository.UserRepository;
import com.fraus.spring.user.repository.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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
        String username = Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getName();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: username=" + username));
        Product product = productRepository.findById(cartDto.productId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found: id=" + cartDto.productId()));

        cartRepository.save(new Cart(
                null,
                user,
                product,
                cartDto.quantity()
        ));
        log.info("User id={} added product id={} in quantity={}", user.getId(), cartDto.productId(), cartDto.quantity());
    }

    public void deleteFromCart(Long id) throws InvalidUserException {
        String username = Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getName();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: username=" + username));
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found: id=" + id));
        if (Objects.equals(cart.getUser().getId(), user.getId()))
            cartRepository.removeById(id);
        else throw new InvalidUserException("Access denied to this resource");
        log.info("Cart id={} has been deleted", id);
    }

    public CartDto updateQuantity(Long id, QuantityRequest quantityRequest) throws InvalidUserException {
        String username = Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getName();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: username=" + username));
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found: id=" + id));
        if (Objects.equals(cart.getUser().getId(), user.getId())) {
            Cart result = cartRepository.save(new Cart(
                    cart.getId(),
                    cart.getUser(),
                    cart.getProduct(),
                    quantityRequest.quantity()
            ));

            log.info("Cart id={} has been updated: new quantity={}", id, quantityRequest.quantity());
            return cartMapper.toDto(result);
        } else throw new InvalidUserException("Access denied to this resource");
    }

    public List<CartDto> findAllProductsFromCart() {
        String username = Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getName();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: username=" + username));
        var cart = cartRepository.findCartByUser_Id(user.getId());

        log.info("Cart are displayed: userId={}", user.getId());
        return cart.stream().map(cartMapper::toDto).toList();
    }
}
