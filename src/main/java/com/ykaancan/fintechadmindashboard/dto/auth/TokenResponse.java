package com.ykaancan.fintechadmindashboard.dto.auth;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        long expiresIn
) {}
