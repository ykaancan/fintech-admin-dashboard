package com.ykaancan.fintechadmindashboard.dto.pricehistory;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreatePriceHistoryRequest(
        @NotNull(message = "Asset ID is required")
        UUID assetId,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,

        String source,

        @NotNull(message = "Recorded at is required")
        LocalDateTime recordedAt
) {}
