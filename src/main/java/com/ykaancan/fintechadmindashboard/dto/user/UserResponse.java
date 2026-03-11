package com.ykaancan.fintechadmindashboard.dto.user;

import com.ykaancan.fintechadmindashboard.enums.Role;
import com.ykaancan.fintechadmindashboard.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        Role role,
        UserStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
