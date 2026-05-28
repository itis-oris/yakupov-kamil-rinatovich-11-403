package com.oris.auth.service;

import com.oris.auth.request.LoginRequest;
import com.oris.auth.model.Account;
import com.oris.auth.userDetails.AuthUserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationSecurityService {

    private final AuthenticationManager authManager;

    public Account authenticate(LoginRequest loginRequest) {
        return ((AuthUserDetailsImpl) authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.email(),
                                loginRequest.password())
                )
                .getPrincipal()
        ).account();
    }
}
