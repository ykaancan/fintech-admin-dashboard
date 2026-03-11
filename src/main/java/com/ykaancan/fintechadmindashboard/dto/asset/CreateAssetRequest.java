package com.ykaancan.fintechadmindashboard.dto.asset;

import com.ykaancan.fintechadmindashboard.enums.AssetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateAssetRequest(
        @NotBlank(message = "Symbol is required")
        String symbol,

        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Asset type is required")
        AssetType assetType,

        BigDecimal currentPrice,

        String currency
) {}
