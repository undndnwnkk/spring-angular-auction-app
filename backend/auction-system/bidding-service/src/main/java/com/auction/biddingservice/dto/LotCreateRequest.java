package com.auction.biddingservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LotCreateRequest(
        @NotNull UUID productId,
        @NotNull @DecimalMin("0.01") BigDecimal price,
        @NotNull @DecimalMin("0.01") BigDecimal minStep,
        @NotNull Instant endTime
        ) {
}
