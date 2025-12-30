package com.auction.notificationservice.kafka;

import com.auction.common.BidPlacedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @KafkaListener(topics = "lot-price-updates", groupId = "notification-group-v2")
    public void listen(BidPlacedEvent bidPlacedEvent) {
        System.out.println("Received from Kafka: " + bidPlacedEvent);
        String destination = "/topic/lot/" + bidPlacedEvent.lotId();
        simpMessagingTemplate.convertAndSend(destination, bidPlacedEvent);
    }

}
