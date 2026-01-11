package com.auction.biddingservice.repository;

import com.auction.biddingservice.model.Bid;
import com.auction.biddingservice.model.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface LotRepository extends JpaRepository<Lot, UUID> {
    @Query("SELECT l FROM Lot l WHERE l.status = 'ACTIVE' AND l.endTime < :now")
    List<Lot> findExpiredActiveLots(@Param("now") Instant now);
}
