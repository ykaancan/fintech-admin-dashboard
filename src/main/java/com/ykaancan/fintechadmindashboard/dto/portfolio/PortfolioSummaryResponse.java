package com.ykaancan.fintechadmindashboard.dto.portfolio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PortfolioSummaryResponse(
        UUID id,
        UUID userId,
        String userName,
        String name,
        String description,
        int positionCount,
        BigDecimal totalValue,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
