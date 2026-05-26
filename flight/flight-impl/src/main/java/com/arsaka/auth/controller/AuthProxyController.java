package com.arsaka.auth.controller;

import com.arsaka.auth.request.LoginRequest;
import com.arsaka.auth.request.RegisterRequest;
import com.arsaka.auth.response.RegisterResponse;
import com.arsaka.auth.service.AuthServiceClient;
import com.arsaka.controller.AuthApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthProxyController implements AuthApi {

    private final AuthServiceClient authServiceClient;

    @Override
    public ResponseEntity<Void> login(LoginRequest request) {
        ResponseEntity<Void> response = authServiceClient.login(request);
        return proxyWithCookies(response);
    }

    @Override
    public ResponseEntity<Void> refresh(String refreshToken) {
        ResponseEntity<Void> response = authServiceClient.refresh(refreshToken);
        return proxyWithCookies(response);
    }

    @Override
    public ResponseEntity<RegisterResponse> register(RegisterRequest request) {
        return authServiceClient.register(request);
    }

    @Override
    public ResponseEntity<Void> logout(String refreshToken) {
        ResponseEntity<Void> response = authServiceClient.logout(refreshToken);
        return proxyWithCookies(response);
    }

    private <T> ResponseEntity<T> proxyWithCookies(ResponseEntity<T> authResponse) {
        List<String> cookies = authResponse.getHeaders().get(HttpHeaders.SET_COOKIE);

        ResponseEntity.BodyBuilder builder = ResponseEntity.status(authResponse.getStatusCode());

        if (cookies != null) {
            cookies.forEach(cookie -> builder.header(HttpHeaders.SET_COOKIE, cookie));
        }

        return builder.body(authResponse.getBody());
    }
}
