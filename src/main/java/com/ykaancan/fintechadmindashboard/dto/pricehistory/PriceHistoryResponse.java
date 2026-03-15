package com.ykaancan.fintechadmindashboard.dto.pricehistory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PriceHistoryResponse(
        UUID id,
        UUID assetId,
        String assetSymbol,
        BigDecimal price,
        String source,
        LocalDateTime recordedAt,
        LocalDateTime createdAt
) {}
