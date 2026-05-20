package com.arsaka.auth.service;

import com.arsaka.auth.exception.ApiException;
import com.arsaka.auth.exception.AuthServiceException;
import com.arsaka.auth.exception.AuthServiceUnavailableException;
import com.arsaka.auth.exception.ValidationException;
import com.arsaka.auth.request.LoginRequest;
import com.arsaka.auth.request.RegisterRequest;
import com.arsaka.auth.request.ValidateRequest;
import com.arsaka.auth.response.AccountResponse;
import com.arsaka.auth.response.RegisterResponse;
import com.arsaka.auth.response.ValidateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthServiceClient {

    private final RestClient restClient;
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private final ObjectMapper mapper;

    public ResponseEntity<Void> login(LoginRequest request) {
        try {
            return restClient
                    .post()
                    .uri("/api/v1/auth/login")
                    .body(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        ApiException apiException = mapper.readValue(
                                res.getBody().readAllBytes(),
                                ApiException.class
                        );
                        throw new AuthServiceException(res.getStatusCode().value(), apiException);
                    })
                    .toBodilessEntity();
        } catch (AuthServiceException e) {
            throw e;

        } catch (Exception exception) {
            log.error("auth login request exception | message={}", exception.getMessage(), exception);
            throw new AuthServiceUnavailableException();
        }
    }

    public ResponseEntity<Void> refresh(String refreshToken) {
        try {

            return restClient
                    .post()
                    .uri("/api/v1/auth/refresh")
                    .header(HttpHeaders.COOKIE, "%s=%s".formatted(REFRESH_TOKEN_COOKIE_NAME, refreshToken))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        ApiException apiException = mapper.readValue(
                                res.getBody().readAllBytes(),
                                ApiException.class
                        );
                        throw new AuthServiceException(res.getStatusCode().value(), apiException);
                    })
                    .toBodilessEntity();
        } catch (AuthServiceException e) {
            throw e;

        } catch (Exception exception) {
            log.error("auth refresh request exception | message={}", exception.getMessage(), exception);
            throw new AuthServiceUnavailableException();
        }

    }

    public ResponseEntity<RegisterResponse> register(RegisterRequest request) {
        try {

            return restClient
                    .post()
                    .uri("/api/v1/auth/register")
                    .body(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        ApiException apiException = mapper.readValue(
                                res.getBody().readAllBytes(),
                                ApiException.class
                        );
                        throw new AuthServiceException(res.getStatusCode().value(), apiException);
                    })
                    .toEntity(RegisterResponse.class);
        } catch (AuthServiceException e) {
            throw e;

        } catch (Exception exception) {
            log.error("auth register request exception | message={}", exception.getMessage(), exception);
            throw new AuthServiceUnavailableException();
        }

    }

    public ResponseEntity<Void> logout(String refreshToken) {
        try {
            return restClient
                    .post()
                    .uri("/api/v1/auth/logout")
                    .header(HttpHeaders.COOKIE, "%s=%s".formatted(REFRESH_TOKEN_COOKIE_NAME, refreshToken))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        ApiException apiException = mapper.readValue(
                                res.getBody().readAllBytes(),
                                ApiException.class
                        );
                        throw new AuthServiceException(res.getStatusCode().value(), apiException);
                    })
                    .toEntity(Void.class);
        } catch (AuthServiceException e) {
            throw e;

        } catch (Exception exception) {
            log.error("auth logout request exception | message={}", exception.getMessage(), exception);
            throw new AuthServiceUnavailableException();
        }

    }

    public ValidateResponse validate(String accessToken, String path, String method) {
        try {
            return restClient
                    .post()
                    .uri("/api/v1/auth/validate")
                    .body(new ValidateRequest(accessToken, path, method))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        ApiException apiException = mapper.readValue(
                                res.getBody().readAllBytes(),
                                ApiException.class
                        );
                        throw new AuthServiceException(res.getStatusCode().value(), apiException);
                    })
                    .body(ValidateResponse.class);
        } catch (AuthServiceException e) {
            throw e;

        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            throw new ValidationException();
        }
    }

    public AccountResponse findAccountById(UUID accountId) {
        try {
            return restClient
                    .get()
                    .uri("/api/v1/account/%s".formatted(accountId))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        ApiException apiException = mapper.readValue(
                                res.getBody().readAllBytes(),
                                ApiException.class
                        );
                        throw new AuthServiceException(res.getStatusCode().value(), apiException);
                    })
                    .body(AccountResponse.class);
        } catch (AuthServiceException e) {
            throw e;
        } catch (Exception exception) {
            log.error("auth get request exception | message={}", exception.getMessage(), exception);
            throw new AuthServiceUnavailableException();
        }
    }

    public AccountResponse updateUsername(UUID accountId, String username) {
        try {
            return restClient
                    .patch()
                    .uri("/api/v1/account/%s".formatted(accountId))
                    .body(Map.of("username", username))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        ApiException apiException = mapper.readValue(
                                res.getBody().readAllBytes(),
                                ApiException.class
                        );
                        throw new AuthServiceException(res.getStatusCode().value(), apiException);
                    })
                    .body(AccountResponse.class);
        } catch (AuthServiceException e) {
            throw e;
        } catch (Exception exception) {
            log.error("auth update username request exception | message={}", exception.getMessage(), exception);
            throw new AuthServiceUnavailableException();
        }
    }
}
