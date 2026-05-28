package com.oris.auth.util;

import com.oris.auth.exception.AuthenticationTokenCookieException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

public class AuthorizationCookieUtil {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";

    public static String getTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new AuthenticationTokenCookieException("There is no Authorization cookie");
        }

        String accessToken = Arrays.stream(cookies)
                .filter(c -> ACCESS_TOKEN_COOKIE_NAME.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new AuthenticationTokenCookieException("There is no Authorization cookie"));


        if (accessToken.isBlank()) {
            throw new AuthenticationTokenCookieException("There is no token in Authorization cookie");
        }

        return accessToken;
    }
}
