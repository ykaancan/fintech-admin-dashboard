package com.ykaancan.fintechadmindashboard.dto.portfolio;

import java.time.LocalDateTime;
import java.util.UUID;

public record PortfolioResponse(
        UUID id,
        UUID userId,
        String userName,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
