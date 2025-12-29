package com.auction.catalogservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequest(@NotBlank String name, @NotBlank String description, @Positive BigDecimal price, @NotNull UUID category_id) {
}
