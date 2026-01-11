package com.auction.biddingservice.repository;

import com.auction.biddingservice.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BidRepository extends JpaRepository<Bid, UUID> {
    Optional<List<Bid>> getBidsByLotId(UUID lotId);

    Optional<Bid> findFirstByLot_IdOrderByBidAmountDesc(UUID lotId);

    @Query("SELECT b FROM Bid b WHERE b.lot.id IN :lotIds ORDER BY b.bidAmount DESC")
    List<Bid> findBidsByLotIds(@Param("lotIds") List<UUID> lotIds);
}
