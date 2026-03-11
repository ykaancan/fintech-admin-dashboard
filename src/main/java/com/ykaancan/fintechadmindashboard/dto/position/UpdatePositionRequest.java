package com.ykaancan.fintechadmindashboard.dto.position;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdatePositionRequest(
        @Positive(message = "Quantity must be positive")
        BigDecimal quantity,

        @Positive(message = "Average cost basis must be positive")
        BigDecimal avgCostBasis
) {}
