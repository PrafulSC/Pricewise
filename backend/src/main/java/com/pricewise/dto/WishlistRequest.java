package com.pricewise.dto;

import jakarta.validation.constraints.NotNull;

public record WishlistRequest(@NotNull Long userId, @NotNull Long productId) {}
