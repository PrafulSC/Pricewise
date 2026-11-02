package com.pricewise.controller;

import com.pricewise.dto.AlertRequest;
import com.pricewise.entity.PriceAlert;
import com.pricewise.service.AlertService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {
    private final AlertService service;
    public AlertController(AlertService service) { this.service = service; }

    @GetMapping("/{userId}") public List<PriceAlert> all(@PathVariable Long userId) {
        return service.getByUser(userId);
    }
    @PostMapping public PriceAlert create(@Valid @RequestBody AlertRequest request) {
        return service.create(request);
    }
}
