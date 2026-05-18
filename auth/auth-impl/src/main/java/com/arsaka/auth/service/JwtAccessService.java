package com.arsaka.auth.service;

import com.arsaka.auth.common.AccountRole;
import com.arsaka.auth.model.Role;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import com.arsaka.auth.config.JwtProperties;
import com.arsaka.auth.dto.AccountAuthData;
import com.arsaka.auth.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class JwtAccessService {

    private final JwtProperties properties;

    private static final String ROLE_KEY = "roles";

    public boolean isValid(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException e) {
            log.warn("Invalid JWT token | type={} | message={}", e.getClass().getSimpleName(), e.getMessage());
            return false;
        }
    }


    public String generate(Account account) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLE_KEY, account.getRoles().stream()
                .map(r -> r.getName().name())
                .toList());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(account.getId().toString())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(properties.accessTokenExpiration())))
                .signWith(SignatureAlgorithm.HS512, properties.secret())
                .compact();
    }

    public AccountAuthData getAccountAuthData(String token) {
        Claims claims = parse(token);
        String accountId = claims.getSubject();
        List<String> roleStrings = (List<String>) claims.get(ROLE_KEY);
        List<AccountRole> roles = roleStrings.stream()
                .map(AccountRole::valueOf)
                .toList();

        return new AccountAuthData(UUID.fromString(accountId), roles);
    }

    private Claims parse(String token) {
        return Jwts.parser()
                .setSigningKey(properties.secret())
                .parseClaimsJws(token)
                .getBody();
    }
}
