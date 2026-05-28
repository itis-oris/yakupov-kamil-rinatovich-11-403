package com.oris.auth.service;

import lombok.RequiredArgsConstructor;
import com.oris.auth.config.JwtProperties;
import com.oris.auth.dto.AccountAuthData;
import com.oris.auth.dto.TokenCouple;
import com.oris.auth.exception.AccountNotFoundException;
import com.oris.auth.exception.InvalidSessionException;
import com.oris.auth.model.Account;
import com.oris.auth.model.RefreshTokenData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final JwtAccessService jwtAccessService;
    private final JwtRefreshService jwtRefreshService;
    private final JwtProperties properties;
    private final AccountService accountService;

    public boolean isValid(String token) {
        return jwtAccessService.isValid(token);
    }

    public AccountAuthData getAccountAuthData(String token) {
        return jwtAccessService.getAccountAuthData(token);
    }

    public TokenCouple generateTokenCouple(Account account) {
        String accessToken = jwtAccessService.generate(account);
        String refreshToken = jwtRefreshService.generate(account);
        return new TokenCouple(accessToken, refreshToken, properties.accessTokenExpiration(), properties.refreshTokenExpiration());
    }

    public TokenCouple generateTokenCouple(String requestRefreshToken) {
        RefreshTokenData refreshTokenData = jwtRefreshService.getAndRemove(requestRefreshToken);

        try {
            Account account = accountService.findAccountById(refreshTokenData.getAccountId());

            accountService.isActive(account);

            String accessToken = jwtAccessService.generate(account);
            String refreshToken = jwtRefreshService.generate(account);

            log.info("Refresh token updated | accountId={}", account.getId());

            return new TokenCouple(accessToken, refreshToken, properties.accessTokenExpiration(), properties.refreshTokenExpiration());
        } catch (AccountNotFoundException exception) {
            log.warn("Account not found exception | accountId={}", refreshTokenData.getAccountId());
            throw new InvalidSessionException();
        }
    }

    public void invalidateRefreshToken(String requestRefreshToken) {
        jwtRefreshService.invalidate(requestRefreshToken);
    }

}
