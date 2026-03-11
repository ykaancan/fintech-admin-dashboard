package com.ykaancan.fintechadmindashboard.dto.user;

import com.ykaancan.fintechadmindashboard.enums.Role;
import com.ykaancan.fintechadmindashboard.enums.UserStatus;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        Role role,
        UserStatus status
) {}
