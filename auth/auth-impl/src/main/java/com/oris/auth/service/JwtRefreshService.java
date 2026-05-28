package com.oris.auth.service;

import lombok.RequiredArgsConstructor;

import com.oris.auth.config.JwtProperties;
import com.oris.auth.model.Account;
import com.oris.auth.model.RefreshTokenData;
import com.oris.auth.repository.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class JwtRefreshService {

    private final RefreshTokenRepository repository;
    private final JwtProperties properties;
    private final RefreshTokenRateLimiter rateLimiter;

    public String generate(Account account) {
        rateLimiter.check(account);

        try {
            if (repository.getCountByAccount(account) >= properties.maxRefreshTokenCount()) {
                repository.removeLast(account);
            }
            String tokenValue = generateTokenValue();
            String tokenHash = hash(tokenValue);

            RefreshTokenData refreshTokenData = new RefreshTokenData();
            refreshTokenData.setAccountId(account.getId());

            repository.save(tokenHash, refreshTokenData);
            return tokenValue;
        } finally {
            rateLimiter.deleteLockKey(account);
        }
    }

    public RefreshTokenData getAndRemove(String refreshToken) {
        return repository.getAndRemove(hash(refreshToken));
    }

    public void invalidate(String requestRefreshToken) {
        String tokenHash = hash(requestRefreshToken);
        repository.remove(tokenHash);
    }

    private String hash(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to hash refresh token | message={}", e.getMessage());
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    private String generateTokenValue() {
        return UUID.randomUUID().toString();
    }
}
