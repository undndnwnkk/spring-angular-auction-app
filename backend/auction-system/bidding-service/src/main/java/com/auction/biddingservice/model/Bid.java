package com.auction.biddingservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bids")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    Lot lot;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "bid_amount", nullable = false)
    @DecimalMin("1")
    private BigDecimal bidAmount;

    @Column(name = "created_at", nullable = false, insertable = false)
    private Instant createdAt;




}
