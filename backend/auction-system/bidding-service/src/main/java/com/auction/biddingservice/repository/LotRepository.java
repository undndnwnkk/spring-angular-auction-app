package com.auction.biddingservice.repository;

import com.auction.biddingservice.model.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LotRepository extends JpaRepository<Lot, UUID> {
}
