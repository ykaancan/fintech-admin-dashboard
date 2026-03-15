package com.ykaancan.fintechadmindashboard.service;

import com.ykaancan.fintechadmindashboard.dto.asset.AssetResponse;
import com.ykaancan.fintechadmindashboard.dto.asset.CreateAssetRequest;
import com.ykaancan.fintechadmindashboard.dto.asset.UpdateAssetRequest;
import com.ykaancan.fintechadmindashboard.dto.common.PagedResponse;
import com.ykaancan.fintechadmindashboard.entity.Asset;
import com.ykaancan.fintechadmindashboard.enums.AssetType;
import com.ykaancan.fintechadmindashboard.exception.DuplicateResourceException;
import com.ykaancan.fintechadmindashboard.exception.ResourceNotFoundException;
import com.ykaancan.fintechadmindashboard.repository.AssetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Transactional
    public AssetResponse createAsset(CreateAssetRequest request) {
        if (assetRepository.existsBySymbol(request.symbol())) {
            throw new DuplicateResourceException("Asset", "symbol", request.symbol());
        }

        Asset asset = Asset.builder()
                .symbol(request.symbol().toUpperCase())
                .name(request.name())
                .assetType(request.assetType())
                .currentPrice(request.currentPrice())
                .currency(request.currency() != null ? request.currency() : "USD")
                .lastPriceUpdate(request.currentPrice() != null ? LocalDateTime.now() : null)
                .build();

        return mapToResponse(assetRepository.save(asset));
    }

    @Transactional(readOnly = true)
    public AssetResponse getAssetById(UUID id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset", "id", id));
        return mapToResponse(asset);
    }

    @Transactional(readOnly = true)
    public AssetResponse getAssetBySymbol(String symbol) {
        Asset asset = assetRepository.findBySymbol(symbol.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Asset", "symbol", symbol));
        return mapToResponse(asset);
    }

    @Transactional(readOnly = true)
    public PagedResponse<AssetResponse> getAllAssets(Pageable pageable) {
        Page<Asset> page = assetRepository.findAll(pageable);
        return PagedResponse.from(page, page.getContent().stream().map(this::mapToResponse).toList());
    }

    @Transactional(readOnly = true)
    public PagedResponse<AssetResponse> getAssetsByType(AssetType type, Pageable pageable) {
        Page<Asset> page = assetRepository.findByAssetType(type, pageable);
        return PagedResponse.from(page, page.getContent().stream().map(this::mapToResponse).toList());
    }

    @Transactional
    public AssetResponse updateAsset(UUID id, UpdateAssetRequest request) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset", "id", id));

        if (request.name() != null) asset.setName(request.name());
        if (request.assetType() != null) asset.setAssetType(request.assetType());
        if (request.currency() != null) asset.setCurrency(request.currency());
        if (request.currentPrice() != null) {
            asset.setCurrentPrice(request.currentPrice());
            asset.setLastPriceUpdate(LocalDateTime.now());
        }

        return mapToResponse(assetRepository.save(asset));
    }

    @Transactional
    public void deleteAsset(UUID id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset", "id", id));
        assetRepository.delete(asset);
    }

    private AssetResponse mapToResponse(Asset asset) {
        return new AssetResponse(
                asset.getId(),
                asset.getSymbol(),
                asset.getName(),
                asset.getAssetType(),
                asset.getCurrentPrice(),
                asset.getCurrency(),
                asset.getLastPriceUpdate(),
                asset.getCreatedAt(),
                asset.getUpdatedAt()
        );
    }
}
