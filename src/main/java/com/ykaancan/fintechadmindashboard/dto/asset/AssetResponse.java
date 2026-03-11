package com.ykaancan.fintechadmindashboard.dto.asset;

import com.ykaancan.fintechadmindashboard.enums.AssetType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AssetResponse(
        UUID id,
        String symbol,
        String name,
        AssetType assetType,
        BigDecimal currentPrice,
        String currency,
        LocalDateTime lastPriceUpdate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
