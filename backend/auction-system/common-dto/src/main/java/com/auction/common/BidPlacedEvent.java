package com.auction.common;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record BidPlacedEvent(UUID lotId, BigDecimal bidAmount, UUID bidderId, Instant createdAt, UUID previousBidderId) {
}
