package com.ykaancan.fintechadmindashboard.service;

import com.ykaancan.fintechadmindashboard.dto.position.CreatePositionRequest;
import com.ykaancan.fintechadmindashboard.dto.position.PositionResponse;
import com.ykaancan.fintechadmindashboard.dto.position.UpdatePositionRequest;
import com.ykaancan.fintechadmindashboard.entity.Asset;
import com.ykaancan.fintechadmindashboard.entity.Portfolio;
import com.ykaancan.fintechadmindashboard.entity.Position;
import com.ykaancan.fintechadmindashboard.exception.DuplicateResourceException;
import com.ykaancan.fintechadmindashboard.exception.ResourceNotFoundException;
import com.ykaancan.fintechadmindashboard.repository.AssetRepository;
import com.ykaancan.fintechadmindashboard.repository.PortfolioRepository;
import com.ykaancan.fintechadmindashboard.repository.PositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final PortfolioRepository portfolioRepository;
    private final AssetRepository assetRepository;

    public PositionService(PositionRepository positionRepository,
                           PortfolioRepository portfolioRepository,
                           AssetRepository assetRepository) {
        this.positionRepository = positionRepository;
        this.portfolioRepository = portfolioRepository;
        this.assetRepository = assetRepository;
    }

    @Transactional
    public PositionResponse createPosition(CreatePositionRequest request) {
        Portfolio portfolio = portfolioRepository.findById(request.portfolioId())
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", request.portfolioId()));
        Asset asset = assetRepository.findById(request.assetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset", "id", request.assetId()));

        positionRepository.findByPortfolioIdAndAssetId(request.portfolioId(), request.assetId())
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Position", "portfolio+asset",
                            request.portfolioId() + "+" + request.assetId());
                });

        Position position = Position.builder()
                .portfolio(portfolio)
                .asset(asset)
                .quantity(request.quantity())
                .avgCostBasis(request.avgCostBasis())
                .build();

        return mapToResponse(positionRepository.save(position));
    }

    @Transactional(readOnly = true)
    public PositionResponse getPositionById(UUID id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Position", "id", id));
        return mapToResponse(position);
    }

    @Transactional(readOnly = true)
    public List<PositionResponse> getPositionsByPortfolioId(UUID portfolioId) {
        return positionRepository.findByPortfolioId(portfolioId)
                .stream().map(this::mapToResponse).toList();
    }

    @Transactional
    public PositionResponse updatePosition(UUID id, UpdatePositionRequest request) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Position", "id", id));

        if (request.quantity() != null) position.setQuantity(request.quantity());
        if (request.avgCostBasis() != null) position.setAvgCostBasis(request.avgCostBasis());

        return mapToResponse(positionRepository.save(position));
    }

    @Transactional
    public void deletePosition(UUID id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Position", "id", id));
        positionRepository.delete(position);
    }

    private PositionResponse mapToResponse(Position position) {
        Asset asset = position.getAsset();
        BigDecimal currentPrice = asset.getCurrentPrice() != null ? asset.getCurrentPrice() : BigDecimal.ZERO;
        BigDecimal marketValue = position.getQuantity().multiply(currentPrice);
        BigDecimal costBasis = position.getQuantity().multiply(position.getAvgCostBasis());
        BigDecimal unrealizedPnl = marketValue.subtract(costBasis);

        return new PositionResponse(
                position.getId(),
                position.getPortfolio().getId(),
                asset.getId(),
                asset.getSymbol(),
                asset.getName(),
                position.getQuantity(),
                position.getAvgCostBasis(),
                currentPrice,
                marketValue,
                unrealizedPnl,
                position.getCreatedAt(),
                position.getUpdatedAt()
        );
    }
}
