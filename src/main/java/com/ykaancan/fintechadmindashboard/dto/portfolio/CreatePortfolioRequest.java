package com.ykaancan.fintechadmindashboard.dto.portfolio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreatePortfolioRequest(
        @NotNull(message = "User ID is required")
        UUID userId,

        @NotBlank(message = "Portfolio name is required")
        String name,

        String description
) {}
