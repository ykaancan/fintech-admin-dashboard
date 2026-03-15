package com.ykaancan.fintechadmindashboard.service;

import com.ykaancan.fintechadmindashboard.dto.transaction.CreateTransactionRequest;
import com.ykaancan.fintechadmindashboard.dto.transaction.TransactionResponse;
import com.ykaancan.fintechadmindashboard.entity.Asset;
import com.ykaancan.fintechadmindashboard.entity.Portfolio;
import com.ykaancan.fintechadmindashboard.entity.Position;
import com.ykaancan.fintechadmindashboard.entity.Transaction;
import com.ykaancan.fintechadmindashboard.exception.InsufficientPositionException;
import com.ykaancan.fintechadmindashboard.exception.ResourceNotFoundException;
import com.ykaancan.fintechadmindashboard.repository.AssetRepository;
import com.ykaancan.fintechadmindashboard.repository.PortfolioRepository;
import com.ykaancan.fintechadmindashboard.repository.PositionRepository;
import com.ykaancan.fintechadmindashboard.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class TradeRecordService {

    private final TransactionRepository transactionRepository;
    private final PositionRepository positionRepository;
    private final PortfolioRepository portfolioRepository;
    private final AssetRepository assetRepository;

    public TradeRecordService(TransactionRepository transactionRepository,
                              PositionRepository positionRepository,
                              PortfolioRepository portfolioRepository,
                              AssetRepository assetRepository) {
        this.transactionRepository = transactionRepository;
        this.positionRepository = positionRepository;
        this.portfolioRepository = portfolioRepository;
        this.assetRepository = assetRepository;
    }

    @Transactional
    public TransactionResponse executeTrade(CreateTransactionRequest request) {
        Portfolio portfolio = portfolioRepository.findById(request.portfolioId())
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", request.portfolioId()));
        Asset asset = assetRepository.findById(request.assetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset", "id", request.assetId()));

        BigDecimal fee = request.fee() != null ? request.fee() : BigDecimal.ZERO;
        BigDecimal totalAmount = request.quantity().multiply(request.price()).add(fee);

        BigDecimal tradeRealizedPnl = null;
        switch (request.type()) {
            case BUY -> handleBuy(portfolio, asset, request.quantity(), request.price());
            case SELL -> tradeRealizedPnl = handleSell(portfolio, asset, request.quantity(), request.price(), fee);
            case DEPOSIT, WITHDRAWAL -> { /* no position impact */ }
        }

        Transaction transaction = Transaction.builder()
                .portfolio(portfolio)
                .asset(asset)
                .type(request.type())
                .quantity(request.quantity())
                .price(request.price())
                .fee(fee)
                .totalAmount(totalAmount)
                .realizedPnl(tradeRealizedPnl)
                .notes(request.notes())
                .executedAt(request.executedAt())
                .build();

        Transaction saved = transactionRepository.save(transaction);
        return mapToResponse(saved);
    }

    private void handleBuy(Portfolio portfolio, Asset asset, BigDecimal buyQty, BigDecimal buyPrice) {
        Optional<Position> existing = positionRepository.findByPortfolioIdAndAssetId(
                portfolio.getId(), asset.getId());

        if (existing.isPresent()) {
            Position pos = existing.get();
            BigDecimal oldQty = pos.getQuantity();
            BigDecimal oldAvg = pos.getAvgCostBasis();

            BigDecimal newQty = oldQty.add(buyQty);
            BigDecimal newAvgCost = oldQty.multiply(oldAvg)
                    .add(buyQty.multiply(buyPrice))
                    .divide(newQty, 4, RoundingMode.HALF_UP);

            pos.setQuantity(newQty);
            pos.setAvgCostBasis(newAvgCost);
            positionRepository.save(pos);
        } else {
            Position newPos = Position.builder()
                    .portfolio(portfolio)
                    .asset(asset)
                    .quantity(buyQty)
                    .avgCostBasis(buyPrice)
                    .realizedPnl(BigDecimal.ZERO)
                    .build();
            positionRepository.save(newPos);
        }
    }

    private BigDecimal handleSell(Portfolio portfolio, Asset asset, BigDecimal sellQty,
                                  BigDecimal sellPrice, BigDecimal fee) {
        Position pos = positionRepository.findByPortfolioIdAndAssetId(
                        portfolio.getId(), asset.getId())
                .orElseThrow(() -> new InsufficientPositionException(
                        "No position exists for asset " + asset.getSymbol() + " in this portfolio"));

        if (sellQty.compareTo(pos.getQuantity()) > 0) {
            throw new InsufficientPositionException(
                    "Cannot sell " + sellQty + " units of " + asset.getSymbol()
                            + ". Current position: " + pos.getQuantity());
        }

        BigDecimal tradeRealizedPnl = sellQty
                .multiply(sellPrice.subtract(pos.getAvgCostBasis()))
                .subtract(fee)
                .setScale(4, RoundingMode.HALF_UP);

        BigDecimal newQty = pos.getQuantity().subtract(sellQty);
        pos.setQuantity(newQty);
        pos.setRealizedPnl(pos.getRealizedPnl().add(tradeRealizedPnl));

        if (newQty.compareTo(BigDecimal.ZERO) == 0) {
            pos.setAvgCostBasis(BigDecimal.ZERO);
        }

        positionRepository.save(pos);
        return tradeRealizedPnl;
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getPortfolio().getId(),
                transaction.getAsset().getId(),
                transaction.getAsset().getSymbol(),
                transaction.getType(),
                transaction.getQuantity(),
                transaction.getPrice(),
                transaction.getFee(),
                transaction.getTotalAmount(),
                transaction.getRealizedPnl(),
                transaction.getNotes(),
                transaction.getExecutedAt(),
                transaction.getCreatedAt()
        );
    }
}
