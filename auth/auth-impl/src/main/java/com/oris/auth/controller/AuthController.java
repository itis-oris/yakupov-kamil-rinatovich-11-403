package com.oris.auth.controller;

import com.oris.auth.dto.TokenCouple;
import com.oris.auth.request.LoginRequest;
import com.oris.auth.request.RegisterRequest;
import com.oris.auth.request.ValidateRequest;
import com.oris.auth.response.RegisterResponse;
import com.oris.auth.response.ValidateResponse;
import com.oris.auth.service.AuthOrchestrationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthController {

    private final AuthOrchestrationService service;

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login request | email={}", loginRequest);
        TokenCouple tokenCouple = service.login(loginRequest);
        return setTokenCookie(tokenCouple);
    }


    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(@NotBlank @CookieValue(REFRESH_TOKEN_COOKIE_NAME) String requestRefreshToken) {
        log.info("Refresh token update request");
        TokenCouple tokenCouple = service.refresh(requestRefreshToken);
        return setTokenCookie(tokenCouple);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Account register request | email={}", registerRequest.email());
        RegisterResponse response = service.register(registerRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@NotBlank @CookieValue(REFRESH_TOKEN_COOKIE_NAME) String requestRefreshToken) {
        service.logout(requestRefreshToken);
        return setEmptyCookie();
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidateResponse> validate(@RequestBody ValidateRequest validateRequest) {
        log.info("validate request | request={}", validateRequest);
        ValidateResponse response = service.validate(validateRequest);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Void> setTokenCookie(TokenCouple tokenCouple) {

        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, tokenCouple.refreshToken())
                .httpOnly(true)
                //.secure(true)
                .path("/")
                .maxAge(Duration.ofMillis(tokenCouple.refreshTokenExpiration()))
                .sameSite("Strict")
                .build();

        ResponseCookie accessCookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, tokenCouple.accessToken())
                .httpOnly(true)
                //.secure(true)
                .path("/")
                .maxAge(Duration.ofMillis(tokenCouple.accessTokenExpiration()))
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .build();
    }

    private ResponseEntity<Void> setEmptyCookie() {

        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                //.secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        ResponseCookie accessCookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, "tokenCouple.accessToken()")
                .httpOnly(true)
                //.secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .build();
    }
}
