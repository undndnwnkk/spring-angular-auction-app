package com.auction.common;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AuctionClosedEvent(UUID lotId, UUID winnerId, BigDecimal finalPrice, LocalDateTime closedAt) {
}
