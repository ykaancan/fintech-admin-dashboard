package com.ykaancan.fintechadmindashboard.dto.portfoliosnapshot;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PortfolioSnapshotResponse(
        UUID id,
        UUID portfolioId,
        BigDecimal totalValue,
        BigDecimal totalCost,
        BigDecimal profitLoss,
        LocalDate snapshotDate,
        LocalDateTime createdAt
) {}
