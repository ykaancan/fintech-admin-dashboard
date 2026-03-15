package com.ykaancan.fintechadmindashboard.security;

import com.ykaancan.fintechadmindashboard.dto.auth.LoginRequest;
import com.ykaancan.fintechadmindashboard.dto.auth.RefreshTokenRequest;
import com.ykaancan.fintechadmindashboard.dto.auth.TokenResponse;
import com.ykaancan.fintechadmindashboard.entity.User;
import com.ykaancan.fintechadmindashboard.enums.UserStatus;
import com.ykaancan.fintechadmindashboard.exception.AuthenticationException;
import com.ykaancan.fintechadmindashboard.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthenticationException("Invalid email or password"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new AuthenticationException("Account is not active");
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new AuthenticationException("Invalid email or password");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new TokenResponse(accessToken, refreshToken, jwtService.getAccessExpiration());
    }

    public TokenResponse refresh(RefreshTokenRequest request) {
        if (!jwtService.isTokenValid(request.refreshToken())) {
            throw new AuthenticationException("Invalid or expired refresh token");
        }

        String userId = jwtService.extractUserId(request.refreshToken());
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new AuthenticationException("Invalid or expired refresh token"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new AuthenticationException("Account is not active");
        }

        String accessToken = jwtService.generateAccessToken(user);

        return new TokenResponse(accessToken, request.refreshToken(), jwtService.getAccessExpiration());
    }
}
