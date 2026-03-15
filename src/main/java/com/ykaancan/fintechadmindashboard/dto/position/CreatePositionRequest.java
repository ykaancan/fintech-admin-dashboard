package com.ykaancan.fintechadmindashboard.dto.position;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePositionRequest(
        @NotNull(message = "Portfolio ID is required")
        UUID portfolioId,

        @NotNull(message = "Asset ID is required")
        UUID assetId,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        BigDecimal quantity,

        @NotNull(message = "Average cost basis is required")
        @Positive(message = "Average cost basis must be positive")
        BigDecimal avgCostBasis
) {}
