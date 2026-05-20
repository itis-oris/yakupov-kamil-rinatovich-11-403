package com.arsaka.auth.service;

import com.arsaka.auth.dto.AccountAuthData;
import com.arsaka.auth.dto.TokenCouple;
import com.arsaka.auth.model.Account;
import com.arsaka.auth.model.Role;
import com.arsaka.auth.request.LoginRequest;
import com.arsaka.auth.request.RegisterRequest;
import com.arsaka.auth.request.ValidateRequest;
import com.arsaka.auth.response.AccountResponse;
import com.arsaka.auth.response.RegisterResponse;
import com.arsaka.auth.response.ValidateResponse;
import com.arsaka.auth.common.AccessDeniedReason;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthOrchestrationService {

    private final JwtService jwtService;
    private final AccountService accountService;
    private final AuthenticationSecurityService authenticationSecurityService;

    @Transactional
    public TokenCouple login(LoginRequest loginRequest) {
        Account account = authenticationSecurityService.authenticate(loginRequest);

        accountService.isActive(account);

        TokenCouple couple = jwtService.generateTokenCouple(account);

        log.info("Account logged in | accountId={}", account.getId());
        return couple;
    }

    @Transactional
    public TokenCouple refresh(String requestRefreshToken) {
        return jwtService.generateTokenCouple(requestRefreshToken);
    }

    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {
        Account account = accountService.save(registerRequest);

        log.info("Account registered | accountId={}", account.getId());
        return new RegisterResponse(
                account.getId(),
                account.getEmail(),
                account.getRoles().stream().map(Role::getName).toList()
        );
    }

    public void logout(String requestRefreshToken) {
        jwtService.invalidateRefreshToken(requestRefreshToken);
    }

    @Transactional
    public AccountResponse findAccountById(UUID id) {
        Account account = accountService.findAccountById(id);
        return new AccountResponse(account.getUsername(), account.getEmail(), account.getCreatedAt(), account.getStatus());
    }

    public ValidateResponse validate(ValidateRequest request) {
        try {
            AccountAuthData accountAuthData = jwtService.getAccountAuthData(request.token());
            return ValidateResponse.ofAllowed(accountAuthData.accountId(), accountAuthData.roles());

        } catch (ExpiredJwtException e) {
            return ValidateResponse.ofNotAllowed(AccessDeniedReason.TOKEN_EXPIRED);
        } catch (JwtException e) {
            return ValidateResponse.ofNotAllowed(AccessDeniedReason.TOKEN_INVALID);
        } catch (Exception exception) {
            log.error("jwt parsing exception | message={}", exception.getMessage(), exception);
            return ValidateResponse.ofNotAllowed(AccessDeniedReason.TOKEN_INVALID);
        }
    }

    @Transactional
    public AccountResponse updateUsername(UUID accountId, String username) {
        Account account = accountService.updateUsername(accountId, username);
        return new AccountResponse(account.getUsername(), account.getEmail(), account.getCreatedAt(), account.getStatus());
    }
}
