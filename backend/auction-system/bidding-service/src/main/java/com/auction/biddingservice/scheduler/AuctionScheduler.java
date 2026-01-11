package com.auction.biddingservice.scheduler;

import com.auction.biddingservice.model.Bid;
import com.auction.biddingservice.model.Lot;
import com.auction.biddingservice.model.LotStatus;
import com.auction.biddingservice.repository.BidRepository;
import com.auction.biddingservice.repository.LotRepository;
import com.auction.common.AuctionClosedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionScheduler {
    private final LotRepository lotRepository;
    private final BidRepository bidRepository;
    private final KafkaTemplate<String, AuctionClosedEvent> kafkaTemplate;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkLotsAndFindWinner() {
        List<Lot> expiredLots = lotRepository.findExpiredActiveLots(Instant.now());
        List<UUID> lotIds = expiredLots.stream().map(Lot::getId).toList();

        List<Bid> allBids = bidRepository.findBidsByLotIds(lotIds);

        Map<UUID, Bid> maxBids = allBids.stream()
                .collect(Collectors.toMap(
                        bid -> bid.getLot().getId(),
                        bid -> bid,
                        (existing, replacement) ->
                                existing.getBidAmount().compareTo(replacement.getBidAmount()) >= 0
                                        ? existing : replacement
                ));

        for (Lot lot : expiredLots) {
            Bid maxBid = maxBids.get(lot.getId());

            UUID winnerId = maxBid == null ? null : maxBid.getUserId();
            BigDecimal finalPrice = maxBid == null ? null : maxBid.getBidAmount();

            AuctionClosedEvent event = new AuctionClosedEvent(lot.getId(), winnerId, finalPrice, LocalDateTime.now());
            kafkaTemplate.send("lot-closed", event);

            log.info("Auction #{} closed. Winner: {}, price: {}", lot.getId(), winnerId, finalPrice);

            lot.setStatus(LotStatus.CLOSED);
            lotRepository.save(lot);
        }

        log.info("Count of finished lots: " + expiredLots.size());
    }
}
