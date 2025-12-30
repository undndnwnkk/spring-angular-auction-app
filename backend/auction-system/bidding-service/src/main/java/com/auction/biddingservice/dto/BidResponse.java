package com.auction.biddingservice.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record BidResponse(UUID userId, BigDecimal bidAmount, Instant createdAt) {
}
