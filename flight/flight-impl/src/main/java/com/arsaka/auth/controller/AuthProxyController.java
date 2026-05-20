package com.arsaka.auth.controller;

import com.arsaka.auth.request.LoginRequest;
import com.arsaka.auth.request.RegisterRequest;
import com.arsaka.auth.response.RegisterResponse;
import com.arsaka.auth.service.AuthServiceClient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthProxyController {

    private final AuthServiceClient authServiceClient;

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest request
    ) {
        ResponseEntity<Void> response = authServiceClient.login(request);
        return proxyWithCookies(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(
            @NotBlank @CookieValue("refreshToken") String refreshToken
    ) {
        ResponseEntity<Void> response = authServiceClient.refresh(refreshToken);
        return proxyWithCookies(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return authServiceClient.register(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @NotBlank @CookieValue("refreshToken") String refreshToken
    ) {
        ResponseEntity<Void> response = authServiceClient.logout(refreshToken);
        return proxyWithCookies(response);
    }

    private <T> ResponseEntity<T> proxyWithCookies(ResponseEntity<T> authResponse) {
        List<String> cookies = authResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        System.out.println("пришли куки: " + cookies);

        ResponseEntity.BodyBuilder builder = ResponseEntity.status(authResponse.getStatusCode());

        if (cookies != null) {
            cookies.forEach(cookie -> builder.header(HttpHeaders.SET_COOKIE, cookie));
        }

        return builder.body(authResponse.getBody());
    }
}
