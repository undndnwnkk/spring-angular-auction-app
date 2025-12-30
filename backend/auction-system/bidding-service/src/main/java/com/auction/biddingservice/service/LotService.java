package com.auction.biddingservice.service;

import com.auction.biddingservice.dto.*;
import com.auction.biddingservice.exception.IncorrectBidInformationException;
import com.auction.biddingservice.exception.IncorrectLotInformationException;
import com.auction.biddingservice.model.Bid;
import com.auction.biddingservice.model.Lot;
import com.auction.biddingservice.model.LotStatus;
import com.auction.biddingservice.repository.BidRepository;
import com.auction.biddingservice.repository.LotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LotService {
    private final LotRepository lotRepository;
    private final BidRepository bidRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final KafkaTemplate<String, BidPlacedEvent> kafkaTemplate;

    @Transactional
    public LotResponse placeBid(LotRequest lotRequest) {
        Lot lot = lotRepository.findById(lotRequest.id())
                .orElseThrow(() -> new IncorrectLotInformationException("Lot not found"));

        if (lot.getStatus() == LotStatus.ACTIVE) {
            BigDecimal minAllowedPrice = lot.getCurrentPrice().add(lot.getMinStep());
            if (lotRequest.price().compareTo(minAllowedPrice) < 0) {
                throw new IncorrectBidInformationException("Bid too low");
            } else {
                lot.setCurrentPrice(lotRequest.price());
            }
        } else throw new IncorrectLotInformationException("Lot is not active");

        Lot newLot = lotRepository.save(lot);
        Bid bid = Bid.builder()
                .lot(lot)
                .userId(UUID.randomUUID())
                .bidAmount(lot.getCurrentPrice())
                .createdAt(Instant.now())
                .build();
        bidRepository.save(bid);

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    String redisKey = createKeyForRedis(newLot.getId());
                    String redisValue = newLot.getCurrentPrice().toString();
                    redisTemplate.opsForValue().set(redisKey, redisValue);

                    kafkaTemplate.send("lot-price-updates", mapToKafkaMessage(bid));
                }
            });
        }

        return mapToResponse(newLot);
    }

    public List<BidResponse> getAllBidsByLotId(UUID lotId) {
        List<Bid> bids = bidRepository.getBidsByLotId(lotId)
                .orElseThrow(() -> new IncorrectLotInformationException("Lot not found"));

        return bids.stream().map(this::mapToResponse).toList();
    }

    @Transactional
    public LotResponse createLot(LotCreateRequest request) {
        Lot lot = Lot.builder()
                .productId(request.productId())
                .currentPrice(request.price())
                .minStep(request.minStep())
                .endTime(request.endTime())
                .status(LotStatus.ACTIVE)
                .build();

        lot = lotRepository.save(lot);
        String redisKey = createKeyForRedis(lot.getId());
        String redisValue = lot.getCurrentPrice().toString();

        redisTemplate.opsForValue().set(redisKey, redisValue);
        return mapToResponse(lot);
    }

    public BigDecimal getCurrentPrice(UUID lotId) {
        String redisKey = createKeyForRedis(lotId);

        String redisPrice = redisTemplate.opsForValue().get(redisKey);
        if (redisPrice != null) {
            return new BigDecimal(redisPrice);
        }

        Lot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> new IncorrectLotInformationException("Lot not found"));

        String redisValue = lot.getCurrentPrice().toString();
        redisTemplate.opsForValue().set(redisKey, redisValue);
        return lot.getCurrentPrice();
    }

    private LotResponse mapToResponse(Lot lot) {
        return LotResponse.builder()
                .id(lot.getId())
                .productId(lot.getProductId())
                .currentPrice(lot.getCurrentPrice())
                .minStep(lot.getMinStep())
                .endTime(lot.getEndTime())
                .status(lot.getStatus().toString()).build();
    }

    private BidResponse mapToResponse(Bid bid) {
        return BidResponse.builder()
                .userId(bid.getUserId())
                .bidAmount(bid.getBidAmount())
                .createdAt(bid.getCreatedAt())
                .build();
    }

    private BidPlacedEvent mapToKafkaMessage(Bid bid) {
        return BidPlacedEvent.builder()
                .lotId(bid.getLot().getId())
                .bidAmount(bid.getBidAmount())
                .bidderId(bid.getUserId())
                .createdAt(bid.getCreatedAt())
                .build();
    }

    private String createKeyForRedis(UUID lotId) {
        return "lot:price:" + lotId.toString();
    }
}
