package com.auction.biddingservice.service;

import com.auction.biddingservice.dto.BidResponse;
import com.auction.biddingservice.dto.LotRequest;
import com.auction.biddingservice.dto.LotResponse;
import com.auction.biddingservice.model.Bid;
import com.auction.biddingservice.model.Lot;
import com.auction.biddingservice.model.LotStatus;
import com.auction.biddingservice.repository.BidRepository;
import com.auction.biddingservice.repository.LotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LotService {
    private final LotRepository lotRepository;
    private final BidRepository bidRepository;

    @Transactional
    public LotResponse placeBid(LotRequest lotRequest) {
        Lot lot = lotRepository.findById(lotRequest.id())
                .orElseThrow(() -> new RuntimeException("Lot not found"));

        if (lot.getStatus() == LotStatus.ACTIVE) {
            BigDecimal minAllowedPrice = lot.getCurrentPrice().add(lot.getMinStep());
            if (lotRequest.price().compareTo(minAllowedPrice) < 0) {
                throw new RuntimeException("Bid too low");
            } else {
                lot.setCurrentPrice(lotRequest.price());
            }
        } else throw new RuntimeException("Lot is not active");

        Lot newLot = lotRepository.save(lot);
        Bid bid = Bid.builder()
                .lot(lot)
                .userId(UUID.randomUUID())
                .bidAmount(lot.getCurrentPrice())
                .build();
        bidRepository.save(bid);

        return mapToResponse(newLot);
    }

    public List<BidResponse> getAllBidsByLotId(UUID lotId) {
        List<Bid> bids = bidRepository.getBidsByLotId(lotId)
                .orElseThrow(() -> new RuntimeException("Bid not found"));

        return bids.stream().map(this::mapToResponse).toList();
    }

    private LotResponse mapToResponse(Lot lot) {
        return LotResponse.builder()
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
}
