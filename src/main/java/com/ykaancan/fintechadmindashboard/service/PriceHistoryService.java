package com.ykaancan.fintechadmindashboard.service;

import com.ykaancan.fintechadmindashboard.dto.common.PagedResponse;
import com.ykaancan.fintechadmindashboard.dto.pricehistory.CreatePriceHistoryRequest;
import com.ykaancan.fintechadmindashboard.dto.pricehistory.PriceHistoryResponse;
import com.ykaancan.fintechadmindashboard.entity.Asset;
import com.ykaancan.fintechadmindashboard.entity.PriceHistory;
import com.ykaancan.fintechadmindashboard.exception.ResourceNotFoundException;
import com.ykaancan.fintechadmindashboard.repository.AssetRepository;
import com.ykaancan.fintechadmindashboard.repository.PriceHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PriceHistoryService {

    private final PriceHistoryRepository priceHistoryRepository;
    private final AssetRepository assetRepository;

    public PriceHistoryService(PriceHistoryRepository priceHistoryRepository, AssetRepository assetRepository) {
        this.priceHistoryRepository = priceHistoryRepository;
        this.assetRepository = assetRepository;
    }

    @Transactional
    public PriceHistoryResponse createPriceHistory(CreatePriceHistoryRequest request) {
        Asset asset = assetRepository.findById(request.assetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset", "id", request.assetId()));

        PriceHistory priceHistory = PriceHistory.builder()
                .asset(asset)
                .price(request.price())
                .source(request.source())
                .recordedAt(request.recordedAt())
                .build();

        return mapToResponse(priceHistoryRepository.save(priceHistory));
    }

    @Transactional(readOnly = true)
    public PriceHistoryResponse getPriceHistoryById(UUID id) {
        PriceHistory priceHistory = priceHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PriceHistory", "id", id));
        return mapToResponse(priceHistory);
    }

    @Transactional(readOnly = true)
    public PagedResponse<PriceHistoryResponse> getPriceHistoryByAssetId(UUID assetId, Pageable pageable) {
        Page<PriceHistory> page = priceHistoryRepository.findByAssetId(assetId, pageable);
        return PagedResponse.from(page, page.getContent().stream().map(this::mapToResponse).toList());
    }

    @Transactional(readOnly = true)
    public List<PriceHistoryResponse> getPriceHistoryByAssetIdAndDateRange(
            UUID assetId, LocalDateTime start, LocalDateTime end) {
        return priceHistoryRepository.findByAssetIdAndRecordedAtBetween(assetId, start, end)
                .stream().map(this::mapToResponse).toList();
    }

    private PriceHistoryResponse mapToResponse(PriceHistory priceHistory) {
        return new PriceHistoryResponse(
                priceHistory.getId(),
                priceHistory.getAsset().getId(),
                priceHistory.getAsset().getSymbol(),
                priceHistory.getPrice(),
                priceHistory.getSource(),
                priceHistory.getRecordedAt(),
                priceHistory.getCreatedAt()
        );
    }
}
