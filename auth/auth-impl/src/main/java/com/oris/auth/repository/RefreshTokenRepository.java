package com.oris.auth.repository;

import com.oris.auth.exception.JwtValidException;
import com.oris.auth.model.Account;
import com.oris.auth.model.RefreshTokenData;
import com.oris.auth.config.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
public class RefreshTokenRepository {

    private final RedisTemplate<String, RefreshTokenData> redisTemplate;
    private final RedisTemplate<String, String> redisSetTemplate;
    private final JwtProperties jwtProperties;

    private static final String TOKEN_KEY = "refresh_token:%s";
    private static final String SESSION_KEY = "account_sessions:%s";

    public RefreshTokenRepository(
            @Qualifier("redisRefreshTokenTemplate") RedisTemplate<String, RefreshTokenData> redisTemplate,
            @Qualifier("redisRefreshTokenSetTemplate") RedisTemplate<String, String> redisSetTemplate,
            JwtProperties jwtProperties
    ) {
        this.redisTemplate = redisTemplate;
        this.redisSetTemplate = redisSetTemplate;
        this.jwtProperties = jwtProperties;
    }

    public void save(String tokenHash, RefreshTokenData refreshTokenData) {

        String tokenKey = TOKEN_KEY.formatted(tokenHash);
        String sessionKey = SESSION_KEY.formatted(refreshTokenData.getAccountId());

        redisTemplate.opsForValue().set(
                tokenKey,
                refreshTokenData,
                Duration.ofMillis(jwtProperties.refreshTokenExpiration())
        );

        redisSetTemplate.opsForZSet().add(
                sessionKey,
                tokenHash,
                refreshTokenData.getCreatedAt().getEpochSecond()
        );

    }

    public RefreshTokenData getAndRemove(String tokenHash) {
        String tokenKey = TOKEN_KEY.formatted(tokenHash);
        RefreshTokenData refreshTokenData = redisTemplate.opsForValue().getAndDelete(tokenKey);

        if (refreshTokenData == null) {
            log.warn("Invalid JWT refresh token | message=token not exists");
            throw new JwtValidException();
        }

        String sessionKey = SESSION_KEY.formatted(refreshTokenData.getAccountId());
        redisSetTemplate.opsForZSet().remove(sessionKey, tokenHash);

        return refreshTokenData;
    }


    public int getCountByAccount(Account account) {
        return getByAccount(account).size();
    }

    public void removeLast(Account account) {
        String sessionKey = SESSION_KEY.formatted(account.getId());

        Set<String> tokenHashSet = redisSetTemplate.opsForZSet().range(sessionKey, 0, 0);

        if (tokenHashSet == null || tokenHashSet.isEmpty()) {
            return;
        }

        String tokenHash = tokenHashSet.stream().findFirst().get();

        String tokenKey = TOKEN_KEY.formatted(tokenHash);

        RefreshTokenData refreshTokenData = redisTemplate.opsForValue().get(tokenKey);

        if (refreshTokenData == null) {
            redisSetTemplate.opsForZSet().remove(sessionKey, tokenHash);
            return;
        }

        redisTemplate.delete(tokenKey);
        redisSetTemplate.opsForZSet().remove(sessionKey, tokenHash);
    }

    public void remove(String tokenHash) {
        String tokenKey = TOKEN_KEY.formatted(tokenHash);

        RefreshTokenData refreshTokenData = redisTemplate.opsForValue().getAndDelete(tokenKey);
        if (refreshTokenData != null) {
            String sessionKey = SESSION_KEY.formatted(refreshTokenData.getAccountId());
            redisSetTemplate.opsForZSet().remove(sessionKey, tokenHash);
        }
    }


    public List<RefreshTokenData> getByAccount(Account account) {
        List<RefreshTokenData> result = new ArrayList<>();
        String sessionKey = SESSION_KEY.formatted(account.getId());

        Set<String> tokenHashSet = redisSetTemplate.opsForZSet().range(sessionKey, 0, -1);

        if (tokenHashSet == null) {
            return result;
        }

        for (String tokenHash: tokenHashSet) {
            String tokenKey = TOKEN_KEY.formatted(tokenHash);
            RefreshTokenData refreshTokenData = redisTemplate.opsForValue().get(tokenKey);

            if (refreshTokenData == null) {
                redisSetTemplate.opsForZSet().remove(sessionKey, tokenHash);
                continue;
            }

            result.add(refreshTokenData);
        }


        return result;
    }
}
