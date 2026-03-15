package com.ykaancan.fintechadmindashboard.service;

import com.ykaancan.fintechadmindashboard.dto.dashboard.AssetPnlResponse;
import com.ykaancan.fintechadmindashboard.dto.dashboard.DailyPnlResponse;
import com.ykaancan.fintechadmindashboard.dto.dashboard.PortfolioDashboardResponse;
import com.ykaancan.fintechadmindashboard.entity.Asset;
import com.ykaancan.fintechadmindashboard.entity.Portfolio;
import com.ykaancan.fintechadmindashboard.entity.PortfolioSnapshot;
import com.ykaancan.fintechadmindashboard.entity.Position;
import com.ykaancan.fintechadmindashboard.exception.ResourceNotFoundException;
import com.ykaancan.fintechadmindashboard.repository.PortfolioRepository;
import com.ykaancan.fintechadmindashboard.repository.PortfolioSnapshotRepository;
import com.ykaancan.fintechadmindashboard.repository.PositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class DashboardService {

    private final PortfolioRepository portfolioRepository;
    private final PositionRepository positionRepository;
    private final PortfolioSnapshotRepository snapshotRepository;

    public DashboardService(PortfolioRepository portfolioRepository,
                            PositionRepository positionRepository,
                            PortfolioSnapshotRepository snapshotRepository) {
        this.portfolioRepository = portfolioRepository;
        this.positionRepository = positionRepository;
        this.snapshotRepository = snapshotRepository;
    }

    @Transactional(readOnly = true)
    public PortfolioDashboardResponse getPortfolioDashboard(UUID portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", portfolioId));

        List<Position> positions = positionRepository.findByPortfolioIdWithAsset(portfolioId);

        BigDecimal totalMarketValue = BigDecimal.ZERO;
        BigDecimal totalCostBasis = BigDecimal.ZERO;
        BigDecimal totalRealizedPnl = BigDecimal.ZERO;

        for (Position pos : positions) {
            BigDecimal currentPrice = pos.getAsset().getCurrentPrice() != null
                    ? pos.getAsset().getCurrentPrice() : BigDecimal.ZERO;
            totalMarketValue = totalMarketValue.add(pos.getQuantity().multiply(currentPrice));
            totalCostBasis = totalCostBasis.add(pos.getQuantity().multiply(pos.getAvgCostBasis()));
            totalRealizedPnl = totalRealizedPnl.add(pos.getRealizedPnl());
        }

        BigDecimal totalUnrealizedPnl = totalMarketValue.subtract(totalCostBasis);
        BigDecimal totalPnl = totalUnrealizedPnl.add(totalRealizedPnl);

        String ownerName = portfolio.getUser().getFirstName() + " " + portfolio.getUser().getLastName();

        return new PortfolioDashboardResponse(
                portfolioId,
                portfolio.getName(),
                ownerName,
                positions.size(),
                totalMarketValue,
                totalCostBasis,
                totalUnrealizedPnl,
                totalRealizedPnl,
                totalPnl,
                LocalDateTime.now()
        );
    }

    @Transactional(readOnly = true)
    public List<AssetPnlResponse> getAssetPnlBreakdown(UUID portfolioId) {
        portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", portfolioId));

        List<Position> positions = positionRepository.findByPortfolioIdWithAsset(portfolioId);

        return positions.stream()
                .map(this::mapToAssetPnl)
                .sorted(Comparator.comparing(AssetPnlResponse::totalPnl).reversed())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DailyPnlResponse> getDailyPnl(UUID portfolioId, LocalDate from, LocalDate to) {
        portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", portfolioId));

        List<PortfolioSnapshot> snapshots = snapshotRepository
                .findByPortfolioIdAndSnapshotDateBetweenOrderBySnapshotDateAsc(portfolioId, from, to);

        List<DailyPnlResponse> result = new ArrayList<>();
        BigDecimal previousPnl = null;

        for (PortfolioSnapshot snapshot : snapshots) {
            BigDecimal dailyChange = previousPnl != null
                    ? snapshot.getProfitLoss().subtract(previousPnl)
                    : null;

            result.add(new DailyPnlResponse(
                    snapshot.getSnapshotDate(),
                    snapshot.getTotalValue(),
                    snapshot.getTotalCost(),
                    snapshot.getProfitLoss(),
                    dailyChange
            ));

            previousPnl = snapshot.getProfitLoss();
        }

        return result;
    }

    private AssetPnlResponse mapToAssetPnl(Position position) {
        Asset asset = position.getAsset();
        BigDecimal currentPrice = asset.getCurrentPrice() != null ? asset.getCurrentPrice() : BigDecimal.ZERO;
        BigDecimal marketValue = position.getQuantity().multiply(currentPrice);
        BigDecimal costBasis = position.getQuantity().multiply(position.getAvgCostBasis());
        BigDecimal unrealizedPnl = marketValue.subtract(costBasis);
        BigDecimal realizedPnl = position.getRealizedPnl();
        BigDecimal totalPnl = unrealizedPnl.add(realizedPnl);

        BigDecimal unrealizedPnlPct = null;
        if (costBasis.compareTo(BigDecimal.ZERO) != 0) {
            unrealizedPnlPct = unrealizedPnl
                    .multiply(BigDecimal.valueOf(100))
                    .divide(costBasis, 2, RoundingMode.HALF_UP);
        }

        return new AssetPnlResponse(
                asset.getId(),
                asset.getSymbol(),
                asset.getName(),
                position.getQuantity(),
                position.getAvgCostBasis(),
                currentPrice,
                marketValue,
                costBasis,
                unrealizedPnl,
                unrealizedPnlPct,
                realizedPnl,
                totalPnl
        );
    }
}
