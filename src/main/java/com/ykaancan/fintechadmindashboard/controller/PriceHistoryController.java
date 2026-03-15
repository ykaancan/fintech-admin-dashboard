package com.ykaancan.fintechadmindashboard.controller;

import com.ykaancan.fintechadmindashboard.dto.common.PagedResponse;
import com.ykaancan.fintechadmindashboard.dto.pricehistory.CreatePriceHistoryRequest;
import com.ykaancan.fintechadmindashboard.dto.pricehistory.PriceHistoryResponse;
import com.ykaancan.fintechadmindashboard.service.PriceHistoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/price-history")
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    public PriceHistoryController(PriceHistoryService priceHistoryService) {
        this.priceHistoryService = priceHistoryService;
    }

    @PostMapping
    public ResponseEntity<PriceHistoryResponse> createPriceHistory(
            @Valid @RequestBody CreatePriceHistoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(priceHistoryService.createPriceHistory(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceHistoryResponse> getPriceHistoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(priceHistoryService.getPriceHistoryById(id));
    }

    @GetMapping
    public ResponseEntity<?> getPriceHistory(
            @RequestParam UUID assetId,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "recordedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        if (startDate != null && endDate != null) {
            List<PriceHistoryResponse> results =
                    priceHistoryService.getPriceHistoryByAssetIdAndDateRange(assetId, startDate, endDate);
            return ResponseEntity.ok(results);
        }

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(priceHistoryService.getPriceHistoryByAssetId(assetId, pageable));
    }
}
