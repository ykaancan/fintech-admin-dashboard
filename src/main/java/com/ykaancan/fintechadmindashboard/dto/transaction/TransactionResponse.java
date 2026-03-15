package com.ykaancan.fintechadmindashboard.dto.transaction;

import com.ykaancan.fintechadmindashboard.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID portfolioId,
        UUID assetId,
        String assetSymbol,
        TransactionType type,
        BigDecimal quantity,
        BigDecimal price,
        BigDecimal fee,
        BigDecimal totalAmount,
        BigDecimal realizedPnl,
        String notes,
        LocalDateTime executedAt,
        LocalDateTime createdAt
) {}
