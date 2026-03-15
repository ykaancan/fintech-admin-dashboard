package com.ykaancan.fintechadmindashboard.service;

import com.ykaancan.fintechadmindashboard.dto.common.PagedResponse;
import com.ykaancan.fintechadmindashboard.dto.transaction.CreateTransactionRequest;
import com.ykaancan.fintechadmindashboard.dto.transaction.TransactionResponse;
import com.ykaancan.fintechadmindashboard.entity.Asset;
import com.ykaancan.fintechadmindashboard.entity.Portfolio;
import com.ykaancan.fintechadmindashboard.entity.Transaction;
import com.ykaancan.fintechadmindashboard.enums.TransactionType;
import com.ykaancan.fintechadmindashboard.exception.ResourceNotFoundException;
import com.ykaancan.fintechadmindashboard.repository.AssetRepository;
import com.ykaancan.fintechadmindashboard.repository.PortfolioRepository;
import com.ykaancan.fintechadmindashboard.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final PortfolioRepository portfolioRepository;
    private final AssetRepository assetRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              PortfolioRepository portfolioRepository,
                              AssetRepository assetRepository) {
        this.transactionRepository = transactionRepository;
        this.portfolioRepository = portfolioRepository;
        this.assetRepository = assetRepository;
    }

    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest request) {
        Portfolio portfolio = portfolioRepository.findById(request.portfolioId())
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", request.portfolioId()));
        Asset asset = assetRepository.findById(request.assetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset", "id", request.assetId()));

        BigDecimal fee = request.fee() != null ? request.fee() : BigDecimal.ZERO;
        BigDecimal totalAmount = request.quantity().multiply(request.price()).add(fee);

        Transaction transaction = Transaction.builder()
                .portfolio(portfolio)
                .asset(asset)
                .type(request.type())
                .quantity(request.quantity())
                .price(request.price())
                .fee(fee)
                .totalAmount(totalAmount)
                .notes(request.notes())
                .executedAt(request.executedAt())
                .build();

        return mapToResponse(transactionRepository.save(transaction));
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(UUID id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));
        return mapToResponse(transaction);
    }

    @Transactional(readOnly = true)
    public PagedResponse<TransactionResponse> getTransactionsByPortfolioId(UUID portfolioId, Pageable pageable) {
        Page<Transaction> page = transactionRepository.findByPortfolioId(portfolioId, pageable);
        return PagedResponse.from(page, page.getContent().stream().map(this::mapToResponse).toList());
    }

    @Transactional(readOnly = true)
    public PagedResponse<TransactionResponse> getTransactionsByPortfolioIdAndType(
            UUID portfolioId, TransactionType type, Pageable pageable) {
        Page<Transaction> page = transactionRepository.findByPortfolioIdAndType(portfolioId, type, pageable);
        return PagedResponse.from(page, page.getContent().stream().map(this::mapToResponse).toList());
    }

    @Transactional(readOnly = true)
    public PagedResponse<TransactionResponse> getTransactionsByPortfolioIdAndAssetId(
            UUID portfolioId, UUID assetId, Pageable pageable) {
        Page<Transaction> page = transactionRepository.findByPortfolioIdAndAssetId(portfolioId, assetId, pageable);
        return PagedResponse.from(page, page.getContent().stream().map(this::mapToResponse).toList());
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
                transaction.getNotes(),
                transaction.getExecutedAt(),
                transaction.getCreatedAt()
        );
    }
}
