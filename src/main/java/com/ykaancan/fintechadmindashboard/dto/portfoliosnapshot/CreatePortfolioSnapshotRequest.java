package com.ykaancan.fintechadmindashboard.dto.portfoliosnapshot;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreatePortfolioSnapshotRequest(
        @NotNull(message = "Portfolio ID is required")
        UUID portfolioId,

        @NotNull(message = "Total value is required")
        BigDecimal totalValue,

        @NotNull(message = "Total cost is required")
        BigDecimal totalCost,

        @NotNull(message = "Profit/loss is required")
        BigDecimal profitLoss,

        @NotNull(message = "Snapshot date is required")
        LocalDate snapshotDate
) {}
