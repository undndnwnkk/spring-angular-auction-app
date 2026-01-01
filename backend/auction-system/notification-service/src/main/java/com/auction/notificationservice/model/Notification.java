package com.auction.notificationservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Document(collection = "notifications")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    @Id
    private String id;

    @Field("user_id")
    private UUID userId;

    @Field("message")
    private String message;

    @Field("lot_id")
    private UUID lotId;

    @Field("bid_amount")
    private BigDecimal bidAmount;

    @Field("type")
    private NotificationType type;

    @Field("created_at")
    private Instant createdAt;

    @Field("is_read")
    private Boolean read;
}
