package com.ykaancan.fintechadmindashboard.dto.dashboard;

import java.math.BigDecimal;
import java.util.UUID;

public record AssetPnlResponse(
        UUID assetId,
        String symbol,
        String assetName,
        BigDecimal quantity,
        BigDecimal avgCostBasis,
        BigDecimal currentPrice,
        BigDecimal marketValue,
        BigDecimal costBasis,
        BigDecimal unrealizedPnl,
        BigDecimal unrealizedPnlPct,
        BigDecimal realizedPnl,
        BigDecimal totalPnl
) {}
