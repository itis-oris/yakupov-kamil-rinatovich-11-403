package com.arsaka.controller;

import com.arsaka.auth.exception.ApiException;
import com.arsaka.auth.request.LoginRequest;
import com.arsaka.auth.request.RegisterRequest;
import com.arsaka.auth.response.RegisterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication",
        description = "API for user authentication management: login, registration, token refresh, logout")
@RestController
@RequestMapping("/api/v1/auth")
@Validated
public interface AuthApi {

    @Operation(summary = "User login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful(sets cookies)"),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ApiException.class))
            ),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = ApiException.class))
            )
    })
    @PostMapping("/login")
    ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest request
    );

    @Operation(summary = "User registration")
    @PostMapping("/register")
    ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request
    );

    @Operation(summary = "token refreshing")
    @PostMapping("/refresh")
    ResponseEntity<Void> refresh(
            @NotBlank @CookieValue("refreshToken") String refreshToken
    );

    @Operation(summary = "logout")
    @PostMapping("/logout")
    ResponseEntity<Void> logout(
            @NotBlank @CookieValue("refreshToken") String refreshToken
    );
}
