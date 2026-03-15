package com.ykaancan.fintechadmindashboard.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PortfolioDashboardResponse(
        UUID portfolioId,
        String portfolioName,
        String ownerName,
        int positionCount,
        BigDecimal totalMarketValue,
        BigDecimal totalCostBasis,
        BigDecimal totalUnrealizedPnl,
        BigDecimal totalRealizedPnl,
        BigDecimal totalPnl,
        LocalDateTime asOf
) {}
