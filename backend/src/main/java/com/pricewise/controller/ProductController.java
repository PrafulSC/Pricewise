package com.pricewise.controller;

import com.pricewise.dto.ProductRequest;
import com.pricewise.entity.Product;
import com.pricewise.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service) { this.service = service; }

    @GetMapping
    public List<Product> all(@RequestParam(required = false) String search,
                              @RequestParam(required = false) Long categoryId) {
        return service.getAll(search, categoryId);
    }

    @GetMapping("/{id}")
    public Product one(@PathVariable Long id) { return service.getOne(id); }

    @PostMapping
    public Product create(@Valid @RequestBody ProductRequest request) { return service.create(request); }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
