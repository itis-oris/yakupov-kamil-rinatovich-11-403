package com.oris.auth.dto;

public record TokenCouple(
        String accessToken,
        String refreshToken,
        long accessTokenExpiration,
        long refreshTokenExpiration
) {}
