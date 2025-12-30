package com.auction.biddingservice.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record LotResponse(UUID id, UUID productId, BigDecimal currentPrice, BigDecimal minStep, Instant endTime, String status) {
}
