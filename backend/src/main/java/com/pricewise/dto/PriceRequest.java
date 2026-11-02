package com.pricewise.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PriceRequest(
    @NotNull Long productId,
    @NotNull Long sellerId,
    @NotNull @DecimalMin("0.01") BigDecimal price,
    String productUrl,
    String availability
) {}
