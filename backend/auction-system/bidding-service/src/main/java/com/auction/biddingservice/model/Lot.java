package com.auction.biddingservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "lots")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Lot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "current_price", nullable = false)
    @DecimalMin("0.01")
    private BigDecimal currentPrice;

    @Column(name = "min_step", nullable = false)
    @DecimalMin("0.01")
    private BigDecimal minStep;

    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LotStatus status;

    @Version
    private Long version;
}
