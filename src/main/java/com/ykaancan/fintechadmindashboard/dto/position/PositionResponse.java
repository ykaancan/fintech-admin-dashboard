package com.ykaancan.fintechadmindashboard.dto.position;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PositionResponse(
        UUID id,
        UUID portfolioId,
        UUID assetId,
        String assetSymbol,
        String assetName,
        BigDecimal quantity,
        BigDecimal avgCostBasis,
        BigDecimal currentPrice,
        BigDecimal marketValue,
        BigDecimal unrealizedPnl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
