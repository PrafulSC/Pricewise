package com.pricewise.service;

import com.pricewise.dto.AlertRequest;
import com.pricewise.entity.*;
import com.pricewise.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AlertService {
    private final PriceAlertRepository alerts;
    private final UserRepository users;
    private final ProductRepository products;

    public AlertService(PriceAlertRepository alerts, UserRepository users, ProductRepository products) {
        this.alerts = alerts;
        this.users = users;
        this.products = products;
    }

    public List<PriceAlert> getByUser(Long userId) {
        return alerts.findByUser_Id(userId);
    }

    public PriceAlert create(AlertRequest request) {
        PriceAlert alert = new PriceAlert();
        alert.setUser(users.findById(request.userId())
            .orElseThrow(() -> new IllegalArgumentException("User not found")));
        alert.setProduct(products.findById(request.productId())
            .orElseThrow(() -> new IllegalArgumentException("Product not found")));
        alert.setTargetPrice(request.targetPrice());
        return alerts.save(alert);
    }
}
