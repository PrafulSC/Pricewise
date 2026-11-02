package com.pricewise.controller;

import com.pricewise.dto.PriceRequest;
import com.pricewise.entity.*;
import com.pricewise.repository.ProductPriceRepository;
import com.pricewise.service.PriceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PriceController {
    private final PriceService service;
    private final ProductPriceRepository prices;
    public PriceController(PriceService service, ProductPriceRepository prices) {
        this.service = service; this.prices = prices;
    }

    @GetMapping("/products/{productId}/prices")
    public List<ProductPrice> prices(@PathVariable Long productId) {
        return service.getPrices(productId);
    }

    @PostMapping("/prices")
    public ProductPrice save(@Valid @RequestBody PriceRequest request) {
        return service.savePrice(request);
    }

    @GetMapping("/products/{productId}/history")
    public List<PriceHistory> history(@PathVariable Long productId) {
        return service.getHistory(productId);
    }

    @GetMapping("/dashboard/stats")
    public java.util.Map<String, Object> stats() {
        return java.util.Map.of(
            "productCount", prices.count() == 0 ? 0 : prices.findAll().stream().map(p -> p.getProduct().getId()).distinct().count(),
            "priceListings", prices.count()
        );
    }
}
