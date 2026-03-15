package com.ykaancan.fintechadmindashboard.dto.transaction;

import com.ykaancan.fintechadmindashboard.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateTransactionRequest(
        @NotNull(message = "Portfolio ID is required")
        UUID portfolioId,

        @NotNull(message = "Asset ID is required")
        UUID assetId,

        @NotNull(message = "Transaction type is required")
        TransactionType type,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        BigDecimal quantity,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,

        @PositiveOrZero(message = "Fee must be zero or positive")
        BigDecimal fee,

        String notes,

        @NotNull(message = "Executed at is required")
        LocalDateTime executedAt
) {}
