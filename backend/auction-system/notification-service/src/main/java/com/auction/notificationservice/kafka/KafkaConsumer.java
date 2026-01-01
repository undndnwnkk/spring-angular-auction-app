package com.auction.notificationservice.kafka;

import com.auction.common.BidPlacedEvent;
import com.auction.notificationservice.model.Notification;
import com.auction.notificationservice.model.NotificationType;
import com.auction.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "lot-price-updates", groupId = "notification-group-v2")
    public void listen(BidPlacedEvent bidPlacedEvent) {
        Notification raiserNotification = Notification.builder()
                .userId(bidPlacedEvent.bidderId())
                .message("You did a bid!")
                .lotId(bidPlacedEvent.lotId())
                .bidAmount(bidPlacedEvent.bidAmount())
                .type(NotificationType.RAISED)
                .createdAt(Instant.now())
                .read(false)
                .build();

        Notification loserNotification = Notification.builder()
                .userId(bidPlacedEvent.bidderId())
                .message("Your bid has been outbid!")
                .lotId(bidPlacedEvent.lotId())
                .bidAmount(bidPlacedEvent.bidAmount())
                .type(NotificationType.RAISED)
                .createdAt(Instant.now())
                .read(false)
                .build();

        notificationRepository.save(raiserNotification);
        notificationRepository.save(loserNotification);

        simpMessagingTemplate.convertAndSend("/topic/lot/" +bidPlacedEvent.lotId(), bidPlacedEvent);

        if (bidPlacedEvent.previousBidderId() != null) {
            String message = "Your bid on lot " + bidPlacedEvent.lotId() + " was outbid!";
            simpMessagingTemplate.convertAndSendToUser(
                    bidPlacedEvent.previousBidderId().toString(),
                    "/queue/notifications",
                    message
            );
        }
    }

}
