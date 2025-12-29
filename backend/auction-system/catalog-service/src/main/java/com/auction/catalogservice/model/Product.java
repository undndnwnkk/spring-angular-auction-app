package com.auction.catalogservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString(exclude = "category")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    private String description;

    @Column(name = "current_price")
    private BigDecimal currentPrice;

    @Column(name = "is_sold", nullable = false)
    private boolean isSold;

    @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "category_id")
    Category category;
}
