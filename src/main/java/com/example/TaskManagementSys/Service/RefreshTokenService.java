package com.example.TaskManagementSys.Service;

import com.example.TaskManagementSys.Entity.RefreshToken;
import com.example.TaskManagementSys.Entity.User;
import com.example.TaskManagementSys.Respository.RefreshTokenRepository;
import com.example.TaskManagementSys.Respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    private static final long REFRESH_TOKEN_DURATION = 7 * 24 * 60 * 60; //7 days in seconds

    public String createRefreshToken(String userName) {
        User user = userRepository.findByUserName(userName);

        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plusSeconds(REFRESH_TOKEN_DURATION);

        RefreshToken refreshToken = new RefreshToken(token, expiryDate, user);
        refreshTokenRepository.save(refreshToken);

        return token;
    }

    public boolean isValidRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                //ensure token is still valid
                .filter(rt -> rt.getExpiryDate().isAfter(Instant.now()))
                .isPresent();
    }

    public String getUsernameFromToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(rt -> rt.getUser().getUserName())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }

    public void revokeRefreshToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }
}