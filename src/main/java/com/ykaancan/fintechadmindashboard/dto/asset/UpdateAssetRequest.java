package com.ykaancan.fintechadmindashboard.dto.asset;

import com.ykaancan.fintechadmindashboard.enums.AssetType;

import java.math.BigDecimal;

public record UpdateAssetRequest(
        String name,
        AssetType assetType,
        BigDecimal currentPrice,
        String currency
) {}
