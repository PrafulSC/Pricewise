package com.pricewise.controller;

import com.pricewise.dto.WishlistRequest;
import com.pricewise.entity.Wishlist;
import com.pricewise.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService service;
    public WishlistController(WishlistService service) { this.service = service; }

    @GetMapping("/{userId}") public List<Wishlist> all(@PathVariable Long userId) {
        return service.getByUser(userId);
    }
    @PostMapping public Wishlist add(@Valid @RequestBody WishlistRequest request) {
        return service.add(request);
    }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.remove(id); }
}
