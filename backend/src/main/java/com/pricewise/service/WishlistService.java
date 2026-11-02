package com.pricewise.service;

import com.pricewise.dto.WishlistRequest;
import com.pricewise.entity.*;
import com.pricewise.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, UserRepository userRepository,
                           ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<Wishlist> getByUser(Long userId) {
        return wishlistRepository.findByUser_Id(userId);
    }

    public Wishlist add(WishlistRequest request) {
        if (wishlistRepository.existsByUser_IdAndProduct_Id(request.userId(), request.productId()))
            throw new IllegalArgumentException("Product already exists in wishlist");
        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Product product = productRepository.findById(request.productId())
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        Wishlist item = new Wishlist();
        item.setUser(user);
        item.setProduct(product);
        return wishlistRepository.save(item);
    }

    public void remove(Long id) {
        wishlistRepository.deleteById(id);
    }
}
