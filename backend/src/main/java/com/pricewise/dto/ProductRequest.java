package com.pricewise.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductRequest(
    @NotBlank String name,
    String brand,
    String description,
    String imageUrl,
    Long categoryId
) {}
