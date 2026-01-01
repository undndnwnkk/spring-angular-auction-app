package com.auction.biddingservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record LotRequest(@NotNull UUID id, @NotNull UUID bidderId, @DecimalMin("0.01") BigDecimal price) {
}
