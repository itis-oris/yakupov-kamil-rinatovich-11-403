package com.arsaka.auth.service;

import com.arsaka.auth.exception.TooManyRequestsException;
import com.arsaka.auth.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class RefreshTokenRateLimiter {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String LOCK_KEY = "lock_sessions:%s";

    public RefreshTokenRateLimiter(
            @Qualifier("redisRefreshTokenSetTemplate") RedisTemplate<String, String> redisTemplate
    ) {
        this.redisTemplate = redisTemplate;
    }

    public void check(Account account) {
        String lockKey = LOCK_KEY.formatted(account.getId());
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", Duration.ofSeconds(5));

        if (Boolean.FALSE.equals(locked)) {
            log.warn("Too many generate refresh token request | accountId={}", account.getId());
            throw new TooManyRequestsException("Too many generate refresh token requests");
        }
    }


    public void deleteLockKey(Account account) {
        String lockKey = LOCK_KEY.formatted(account.getId());
        redisTemplate.delete(lockKey);
    }
}
