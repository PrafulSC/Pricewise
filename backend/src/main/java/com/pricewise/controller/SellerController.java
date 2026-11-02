package com.pricewise.controller;

import com.pricewise.dto.SellerRequest;
import com.pricewise.entity.Seller;
import com.pricewise.repository.SellerRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {
    private final SellerRepository repository;
    public SellerController(SellerRepository repository) { this.repository = repository; }

    @GetMapping public List<Seller> all() { return repository.findAll(); }

    @PostMapping
    public Seller create(@Valid @RequestBody SellerRequest request) {
        Seller seller = new Seller();
        seller.setSellerName(request.sellerName());
        seller.setWebsite(request.website());
        return repository.save(seller);
    }
}
