package com.pricewise.dto;

import jakarta.validation.constraints.NotBlank;

public record SellerRequest(@NotBlank String sellerName, String website) {}
