package com.auction.biddingservice.controller;

import com.auction.biddingservice.dto.BidResponse;
import com.auction.biddingservice.dto.LotCreateRequest;
import com.auction.biddingservice.dto.LotRequest;
import com.auction.biddingservice.dto.LotResponse;
import com.auction.biddingservice.service.LotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bidding")
@RequiredArgsConstructor
public class LotController {
    private final LotService lotService;

    @GetMapping("/lots/{lotId}/history")
    public ResponseEntity<List<BidResponse>> getLotHistory(@PathVariable UUID lotId) {
        List<BidResponse> result = lotService.getAllBidsByLotId(lotId);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{lotId}/price")
    public ResponseEntity<BigDecimal> getCurrentPrice(@PathVariable UUID lotId) {
        BigDecimal result = lotService.getCurrentPrice(lotId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/place")
    public ResponseEntity<LotResponse> placeBid(@Valid @RequestBody LotRequest lotRequest, @RequestHeader(value = "X-User-Id", required = true) String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID header is missing or empty");
        }
        return ResponseEntity.ok(lotService.placeBid(lotRequest, userId));
    }

    @PostMapping
    public ResponseEntity<LotResponse> placeLot(@Valid @RequestBody LotCreateRequest lotRequest) {
        return ResponseEntity.ok(lotService.createLot(lotRequest));
    }
}
