package com.pricewise.controller;

import com.pricewise.entity.Category;
import com.pricewise.repository.CategoryRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository repository;
    public CategoryController(CategoryRepository repository) { this.repository = repository; }
    @GetMapping public List<Category> all() { return repository.findAll(); }
}
