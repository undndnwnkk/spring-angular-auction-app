package com.auction.biddingservice.repository;

import com.auction.biddingservice.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BidRepository extends JpaRepository<Bid, UUID> {
    Optional<List<Bid>> getBidsByLotId(UUID lotId);
}
