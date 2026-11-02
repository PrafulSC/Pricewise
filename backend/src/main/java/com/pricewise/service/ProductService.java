package com.pricewise.service;

import com.pricewise.dto.ProductRequest;
import com.pricewise.entity.Category;
import com.pricewise.entity.Product;
import com.pricewise.repository.CategoryRepository;
import com.pricewise.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository products;
    private final CategoryRepository categories;

    public ProductService(ProductRepository products, CategoryRepository categories) {
        this.products = products;
        this.categories = categories;
    }

    public List<Product> getAll(String search, Long categoryId) {
        if (categoryId != null) return products.findByCategory_Id(categoryId);
        if (search != null && !search.isBlank())
            return products.findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(search, search);
        return products.findAll();
    }

    public Product getOne(Long id) {
        return products.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    public Product create(ProductRequest request) {
        Product product = new Product();
        apply(product, request);
        return products.save(product);
    }

    public Product update(Long id, ProductRequest request) {
        Product product = getOne(id);
        apply(product, request);
        return products.save(product);
    }

    public void delete(Long id) {
        products.delete(getOne(id));
    }

    private void apply(Product product, ProductRequest request) {
        product.setName(request.name());
        product.setBrand(request.brand());
        product.setDescription(request.description());
        product.setImageUrl(request.imageUrl());
        if (request.categoryId() != null) {
            Category category = categories.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }
    }
}
