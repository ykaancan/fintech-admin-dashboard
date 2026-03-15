package com.ykaancan.fintechadmindashboard.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyPnlResponse(
        LocalDate date,
        BigDecimal totalValue,
        BigDecimal totalCost,
        BigDecimal profitLoss,
        BigDecimal dailyChange
) {}
